package com.vk.ai.template.impl;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.SearchInfo;
import com.alibaba.dashscope.aigc.generation.SearchOptions;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import com.alibaba.fastjson2.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mybatisflex.core.query.QueryWrapper;
import com.vk.ai.domain.ChatMessage;
import com.vk.ai.domain.ModelList;
import com.vk.ai.domain.dto.GeneralMessageDto;
import com.vk.ai.enums.AiType;
import com.vk.ai.enums.MessageStatus;
import com.vk.ai.mapper.ChatMessageMapper;
import com.vk.ai.template.AbstractAiTemplate;
import com.vk.common.core.utils.uuid.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.vk.ai.domain.table.ChatMessageTableDef.CHAT_MESSAGE;

@Service
@Slf4j(topic = "BL_ChatMessageTemplateImpl")
public class BLChatMessageTemplateImpl extends AbstractAiTemplate {
    @Override
    public AiType support() {
        return AiType.BL;
    }

    @Autowired
    private ChatMessageMapper chatMessageMapper;

    private static boolean isFirstPrint = true;

    /**
     * @param dto       消息
     * @param modelList 模型
     * @return
     */
    @Override
    public SseEmitter chatMessage(GeneralMessageDto dto, ModelList modelList) {
        UUID uuid = UUID.randomUUID();

        String modelId = modelList.getModelId();

        String prompt = dto.getPrompt();
        Boolean searchEnabled = dto.getSearchEnabled();

        // 搜索是否首次
        SseEmitter emitter = new SseEmitter(60_000L); // 设置1分钟超时
        Generation gen = new Generation();

        List<Message> messageList = getHistoryMessageList(dto, 10);

        //消息持久化
        StringBuilder dbReasoningContent = new StringBuilder();
        StringBuilder dbContent = new StringBuilder();
        StringBuilder search = new StringBuilder();

        try {

            // 定义搜索策略
            SearchOptions searchOptions = SearchOptions.builder()
                    // 使返回结果中包含搜索信息的来源
                    .enableSource(true)
                    // 强制开启互联网搜索
                    .forcedSearch(true)
                    // 开启角标标注
                    .enableCitation(true)
                    // 设置角标标注样式为[ref_i]
                    .citationFormat("[ref_<number>]")
                    // 联网搜索10条信息
                    .searchStrategy("pro")
                    .build();

            GenerationParam build = GenerationParam.builder()
                    // 若没有配置环境变量，请用百炼API Key将下行替换为：.apiKey("sk-xxx")
                    .apiKey("sk-aff40a9d4b8a414ea461237d1fb6adc8")
                    // 此处以 qwq-plus 为例，可按需更换模型名称
                    .model(modelId)
                    .messages(messageList)
                    // 开启联网搜索的参数
                    .enableSearch(searchEnabled)
                    .incrementalOutput(true)
                    .resultFormat(GenerationParam.ResultFormat.MESSAGE)
                    .searchOptions(searchOptions)
                    .build();


            gen.streamCall(build).doOnError(error -> {
                        log.error("streamChatCompletion uuid:{} 意外出错:{} ", uuid, error.getMessage());
                        try {
                            emitter.send("Error occurred: " + error.getMessage(), MediaType.TEXT_EVENT_STREAM);
                        } catch (IOException e) {
                            emitter.completeWithError(e);
                            log.error("emitter send uuid:{} error:{} ", uuid, e.getMessage());
                        }
                    })
                    .flatMap(delta -> {
                        if (!delta.getOutput().getChoices().isEmpty()) {
                            String reasoning = delta.getOutput().getChoices().get(0).getMessage().getReasoningContent();
                            ObjectNode objectNode = new ObjectMapper().createObjectNode();

                            if (!reasoning.isEmpty()) {
                                // 开启搜索
                                if (searchEnabled) {
                                    if (isFirstPrint) {
                                        SearchInfo info = delta.getOutput().getSearchInfo();
                                        if (null != info) {
                                            List<SearchInfo.SearchResult> searchInfo = info.getSearchResults();
                                            if (null != searchInfo) {
                                                isFirstPrint = false;
                                                objectNode.put("s", JSON.toJSONString(searchInfo));
                                                search.append(searchInfo);
                                            }
                                        }
                                    }
                                }
                                dbReasoningContent.append(reasoning);
                                objectNode.put("r", reasoning);
                            } else {
                                String content = delta.getOutput().getChoices().get(0).getMessage().getContent();
                                if (!content.isEmpty()) {
                                    dbContent.append(content);
                                    objectNode.put("v", content);
                                }
                            }
                            return Mono.just(objectNode.toString());
                        }
                        return Mono.empty();
                    })
                    .doOnNext(pushContent -> {
                        try {
                            emitter.send(pushContent, MediaType.TEXT_EVENT_STREAM);
                        } catch (IOException e) {
                            emitter.completeWithError(e);
                            log.error("emitter doOnNext send uuid:{} error:{} ", uuid, e.getMessage());
                        }
                    })
                    .doOnTerminate(emitter::complete)
                    .subscribe();

            emitter.onCompletion(() -> {

                log.info("SSE流已完成 uuid:{}", uuid);
            });

            emitter.onError(e -> {
                emitter.completeWithError(e);
                log.error("SSE流发生错误 uuid:{} error:{}", uuid, e.getMessage());
            });

        } catch (Exception e) {
            emitter.completeWithError(e);
            log.error("终止或网络异常 uuid:{} error:{}", uuid, e.getMessage());
        }

        return emitter;
    }

    private List<Message> getHistoryMessageList(GeneralMessageDto dto, int size) {
        if (size == 0) {
            size = 10;
        }

        String sessionId = dto.getChatSessionId();

        QueryWrapper wrapper = QueryWrapper.create().select(
                CHAT_MESSAGE.ROLE,
                CHAT_MESSAGE.CONTENT
        ).where(
                CHAT_MESSAGE.INFO_ID.eq(sessionId)
                        .and(CHAT_MESSAGE.DEL.eq(false))
                        .and(CHAT_MESSAGE.STATUS.eq(MessageStatus.SUCCESS.toString()))
        ).orderBy(CHAT_MESSAGE.UPDATE_TIME, false).limit(size);

        List<ChatMessage> messageList = chatMessageMapper.selectListByQuery(wrapper);

        List<Message> messages = messageList.stream()
                .map(chatMessage -> Message.builder()
                        .role(chatMessage.getRole())
                        .content(chatMessage.getContent())
                        .build())
                .collect(Collectors.toCollection(ArrayList::new));

        messages.add(Message.builder()
                .role(Role.USER.getValue())
                .content(dto.getPrompt())
                .build());

        return messages;
    }


}
