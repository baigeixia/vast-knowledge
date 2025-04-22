package com.vk.ai.template.impl;

import com.alibaba.dashscope.aigc.generation.*;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import com.alibaba.fastjson2.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.vk.ai.config.TxConfig;
import com.vk.ai.domain.ChatMessage;
import com.vk.ai.domain.ModelList;
import com.vk.ai.domain.dto.GeneralMessageDto;
import com.vk.ai.enums.AiType;
import com.vk.ai.enums.MessageStatus;
import com.vk.ai.mapper.ChatInfoMapper;
import com.vk.ai.mapper.ChatMessageMapper;
import com.vk.ai.template.AbstractAiTemplate;
import com.vk.common.core.exception.LeadNewsException;
import com.vk.common.core.utils.StringUtils;
import com.vk.common.core.utils.uuid.UUID;
import com.vk.db.domain.aiMessage.AiMg;
import com.vk.db.repository.aiMessage.AiMgRepository;
import io.reactivex.Flowable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Service
@Slf4j(topic = "BL_ChatMessageTemplateImpl")
public class BLChatMessageTemplateImpl extends AbstractAiTemplate {
    @Override
    public AiType support() {
        return AiType.BL;
    }

    @Autowired
    private ChatMessageMapper chatMessageMapper;

    @Autowired
    private ChatInfoMapper chatInfoMapper;

    @Autowired
    private AiMgRepository aiMgRepository;

    @Autowired
    private MessageTemplate messageTemplate;

    @Autowired
    private TxConfig txConfig;

    /**
     * @param dto       消息
     * @param modelList 模型
     * @return
     */
    @Override
    public SseEmitter chatMessage(GeneralMessageDto dto, ModelList modelList) {
        UUID uuid = UUID.randomUUID();

        String modelId = modelList.getModelId();
        String sessionId = dto.getChatSessionId();

        Boolean searchEnabled = dto.getSearchEnabled();
        String prompt = dto.getPrompt();

        // 用户信息 search 和 reasoning 不参与
        // serveDBMessage(Role.USER.getValue(), modelList, dto, null, null, prompt, MessageStatus.SUCCESS);

        messageTemplate.saveMessage(Role.USER.getValue(), modelList, dto, null, null, prompt, MessageStatus.SUCCESS);

        Generation gen = new Generation();
        // 对话历史
        List<Message> messageList = getHistoryMessageList(dto, 5);

        // 消息持久化
        StringBuffer dbReasoningContent = new StringBuffer();
        StringBuffer dbContent = new StringBuffer();
        StringBuffer search = new StringBuffer();

        final AtomicBoolean isFirstPrint = new AtomicBoolean(true);

        // try {
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
                .apiKey(txConfig.getKey())
                // 此处以 qwq-plus 为例，可按需更换模型名称
                .model(modelId)
                .messages(messageList)
                // 开启联网搜索的参数
                .enableSearch(searchEnabled)
                // 开启流
                .incrementalOutput(true)
                .searchOptions(searchOptions)
                .resultFormat(GenerationParam.ResultFormat.MESSAGE)
                .build();
        // 消息父级id
        Integer messageId = dto.getParentMessageId();

        if (messageId == 1) {
            String title;
            GenerationResult call = null;
            try {
                call = gen.call(GenerationParam.builder()
                        // 若没有配置环境变量，请用百炼API Key将下行替换为：.apiKey("sk-xxx")
                        .apiKey("sk-aff40a9d4b8a414ea461237d1fb6adc8")
                        // 此处以 qwq-plus 为例，可按需更换模型名称
                        .model(modelId)
                        .messages(Collections.singletonList(
                                Message.builder()
                                        .role(Role.SYSTEM.getValue())
                                        .content("请为下面这段对话生成一个简洁的标题（不超过10个字）：" + prompt)
                                        .build()))
                        .resultFormat(GenerationParam.ResultFormat.MESSAGE)
                        .build());

            } catch (Exception e) {
                log.error("api获取标题失败 uuid:{} error:{}", uuid, e.getMessage());
                throw new LeadNewsException("服务器繁忙，请稍后再试。");
            }

            List<GenerationOutput.Choice> choices = call.getOutput().getChoices();
            if (null == choices || choices.isEmpty()) {
                title = prompt.substring(0, 10);
            } else {
                title = choices.get(0).getMessage().getContent();
            }

            chatInfoMapper.updateMessageTitle(sessionId, title);
        }

        Flowable<GenerationResult> resultFlowable = null;
        try {
            resultFlowable = gen.streamCall(build);
        } catch (Exception e) {
            messageTemplate.saveMessage(Role.ASSISTANT.getValue(), modelList, dto, search.toString(), dbReasoningContent.toString(), dbContent.toString(), MessageStatus.FAILED);
            log.error("调用api失败 uuid:{} error:{}", uuid, e.getMessage());
            throw new LeadNewsException("服务器繁忙，请稍后再试。");
        }

        SseEmitter emitter = SseStreamHandler.<GenerationResult>builder()
                .uuid(uuid)
                .flowable(resultFlowable)
                .contentMapper(chunk -> {
                    List<GenerationOutput.Choice> choices = chunk.getOutput().getChoices();
                    if (choices == null || choices.isEmpty()) return Optional.empty();
                    Message message = choices.get(0).getMessage();

                    String reasoning = message.getReasoningContent();

                    ObjectNode objectNode = new ObjectMapper().createObjectNode();
                    if (StringUtils.isNotBlank(reasoning)) {
                        // 开启搜索
                        if (searchEnabled) {
                            // 搜索是否首次
                            if (isFirstPrint.get()) {
                                SearchInfo info = chunk.getOutput().getSearchInfo();
                                if (null != info) {
                                    List<SearchInfo.SearchResult> searchInfo = info.getSearchResults();
                                    if (null != searchInfo) {
                                        isFirstPrint.set(false);
                                        search.append(searchInfo);
                                        objectNode.put("s", JSON.toJSONString(searchInfo));
                                    }
                                }
                            }
                        }
                        dbReasoningContent.append(reasoning);
                        objectNode.put("r", reasoning);
                    } else {
                        String content = message.getContent();
                        if (!content.isEmpty()) {
                            dbContent.append(content);
                            objectNode.put("v", content);
                        }
                    }


                    return Optional.of(objectNode.toString());
                })
                .onComplete(() -> {
                    log.info("思考内容：{}", dbReasoningContent);
                    log.info("内容：{}", dbContent);
                    messageTemplate.saveMessage(Role.ASSISTANT.getValue(), modelList, dto, search.toString(), dbReasoningContent.toString(), dbContent.toString(), MessageStatus.SUCCESS);
                    log.info("SSE流已完成 uuid:{}", uuid);
                })
                .onError(error -> {
                    // serveDBMessage(Role.ASSISTANT.getValue(), modelList, dto, search.toString(), dbReasoningContent.toString(), dbContent.toString(), MessageStatus.FAILED);
                    messageTemplate.saveMessage(Role.ASSISTANT.getValue(), modelList, dto, search.toString(), dbReasoningContent.toString(), dbContent.toString(), MessageStatus.FAILED);
                    log.error("SSE 流异常 uuid:{} error:{}", uuid, error.getMessage());
                })
                .onTimeout(() -> {
                    log.error("SSE流超时 uuid:{} ", uuid);
                    messageTemplate.saveMessage(Role.ASSISTANT.getValue(), modelList, dto, search.toString(), dbReasoningContent.toString(), dbContent.toString(), MessageStatus.OUT);
                })
                .onEmitterError(error -> {
                    log.error("emitter 推送失败 uuid:{} error:{}", uuid, error.getMessage());
                })
                .build();


    /*        resultFlowable.doOnError(error -> {
                        log.error("streamChatCompletion uuid:{} 意外出错:{} ", uuid, error.getMessage());
                        try {
                            emitter.send("Error occurred: " + error.getMessage(), MediaType.TEXT_EVENT_STREAM);
                        } catch (IOException e) {
                            emitter.completeWithError(e);
                            log.error("emitter send uuid:{} error:{} ", uuid, e.getMessage());
                        }
                    })
                    .flatMap(delta -> {
                        List<GenerationOutput.Choice> choices = delta.getOutput().getChoices();
                        if (choices == null || choices.isEmpty()) return Mono.empty();

                        Message message = choices.get(0).getMessage();

                        String reasoning = message.getReasoningContent();
                        ObjectNode objectNode = new ObjectMapper().createObjectNode();

                        if (StringUtils.isNotBlank(reasoning)) {
                            // 开启搜索
                            if (searchEnabled) {
                                // 搜索是否首次
                                if (isFirstPrint.get()) {
                                    SearchInfo info = delta.getOutput().getSearchInfo();
                                    if (null != info) {
                                        List<SearchInfo.SearchResult> searchInfo = info.getSearchResults();
                                        if (null != searchInfo) {
                                            isFirstPrint.set(false);
                                            search.append(searchInfo);
                                            objectNode.put("s", JSON.toJSONString(searchInfo));
                                        }
                                    }
                                }
                            }
                            dbReasoningContent.append(reasoning);
                            objectNode.put("r", reasoning);
                        } else {
                            String content = message.getContent();
                            if (!content.isEmpty()) {
                                dbContent.append(content);
                                objectNode.put("v", content);
                            }
                        }
                        return Mono.just(objectNode.toString());

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
                    .subscribe();*/


        // } catch (Exception e) {
        //     emitter.completeWithError(e);
        //     serveDBMessage(Role.ASSISTANT.getValue(), modelList, dto, search.toString(), dbReasoningContent.toString(), dbContent.toString(), MessageStatus.FAILED);
        //     log.error("终止或网络异常 uuid:{} error:{}", uuid, e.getMessage());
        // }

  /*      emitter.onCompletion(() -> {
            log.info("思考内容：{}", dbReasoningContent);
            log.info("内容：{}", dbContent);
            serveDBMessage(Role.ASSISTANT.getValue(), modelList, dto, search.toString(), dbReasoningContent.toString(), dbContent.toString(), MessageStatus.SUCCESS);
            log.info("SSE流已完成 uuid:{}", uuid);
            emitterMap.remove(uuid.toString());
        });

        emitter.onTimeout(() -> {
            log.error("SSE流超时 uuid:{} ", uuid);
            emitterMap.remove(uuid.toString());
        });

        emitter.onError(e -> {
            emitter.completeWithError(e);
            serveDBMessage(Role.ASSISTANT.getValue(), modelList, dto, search.toString(), dbReasoningContent.toString(), dbContent.toString(), MessageStatus.FAILED);
            log.error("SSE流发生错误 uuid:{} error:{}", uuid, e.getMessage());
            emitterMap.remove(uuid.toString());
        });*/


        return emitter;
    }


    private void serveDBMessage(String role, ModelList modelList, GeneralMessageDto dto, String searchContent, String reasoningContent, String content, MessageStatus status) {
        AiMg aiMg = buildChatMessage(role, modelList, dto, searchContent, reasoningContent, content, status);
        try {
            aiMgRepository.save(aiMg);
            chatInfoMapper.updateMessageIdAuto(aiMg.getInfoId());
            log.debug("保存消息成功: {}", aiMg);
        } catch (Exception e) {
            log.error("保存消息失败: {}", aiMg, e);
            throw new RuntimeException("数据库保存失败", e);
        }
    }


    private AiMg buildChatMessage(String role, ModelList modelList, GeneralMessageDto dto, String searchContent,
                                         String reasoningContent, String content, MessageStatus messageStatus) {

        Boolean searchEnabled = dto.getSearchEnabled();

        String searchStatus = null;
        // 开启搜索 并且 有搜索返回 SUCCESS
        if (searchEnabled) {
            searchStatus = searchContent.isEmpty() ? MessageStatus.FAILED.toString() : MessageStatus.SUCCESS.toString();
        }
        // String searchStatus = searchEnabled && !searchContent.isEmpty() ? MessageStatus.SUCCESS.toString() : MessageStatus.FAILED.toString();
        LocalDateTime now = LocalDateTime.now();

        Integer messageId = dto.getParentMessageId();
        // 这里是控制 父级 id 递增
        dto.setParentMessageId((messageId == null) ? 1 : messageId + 1);

        AiMg aiMg = new AiMg();
        aiMg.setInfoId(dto.getChatSessionId());
        aiMg.setMessageId(dto.getParentMessageId());
        aiMg.setParentId(messageId);
        aiMg.setModelId(modelList.getModelId());
        aiMg.setModelName(modelList.getModelName());
        aiMg.setRole(role);
        aiMg.setContent(content);
        aiMg.setThinkingEnabled(dto.getThinkingEnabled());
        aiMg.setThinkingContent(reasoningContent);
        aiMg.setSearchEnabled(searchEnabled);
        aiMg.setSearchStatus(searchStatus);
        aiMg.setSearchResults(searchContent);
        aiMg.setCreatingTime(now);
        aiMg.setUpdateTime(now);
        aiMg.setDel(false);
        aiMg.setStatus(messageStatus.toString());
        return aiMg;
    }

    private List<Message> getHistoryMessageList(GeneralMessageDto dto, int size) {
        String sessionId = dto.getChatSessionId();
        List<AiMg> messageList = messageTemplate.getOrderedMessages( sessionId, size);

        // Pageable pageable = PageRequest.of(0, size); // size 为返回条数
        // List<AiMg> parentIdDesc = aiMgRepository.findByInfoIdAndDelFalseOrderByParentIdDesc(sessionId, pageable);
        //
        // List<AiMg> messageList = parentIdDesc.stream()
        //         .sorted(Comparator.comparing(AiMg::getParentId, Comparator.nullsLast(Comparator.naturalOrder())))
        //         .toList();

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
