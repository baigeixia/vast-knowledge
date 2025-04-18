package com.vk.ai.template.impl;

import com.alibaba.dashscope.common.Role;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.vk.ai.domain.ModelList;
import com.vk.ai.domain.dto.GeneralMessageDto;
import com.vk.ai.enums.AiType;
import com.vk.ai.enums.MessageStatus;
import com.vk.ai.mapper.ChatInfoMapper;
import com.vk.ai.mapper.ChatMessageMapper;
import com.vk.ai.template.AbstractAiTemplate;
import com.vk.common.core.utils.StringUtils;
import com.vk.common.core.utils.threads.TaskVirtualExecutorUtil;
import com.vk.common.core.utils.uuid.UUID;
import com.vk.db.domain.aiMessage.AiMg;
import com.volcengine.ark.runtime.model.completion.chat.*;
import com.volcengine.ark.runtime.service.ArkService;
import io.reactivex.Flowable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 服务层实现。
 *
 * @author 张三
 * @since 2025-03-31
 */
@Service
@Slf4j(topic = "FZ_ChatMessageTemplateImpl")
public class FZChatMessageTemplateImpl extends AbstractAiTemplate {

    @Override
    public AiType support() {
        return AiType.FZ;
    }

    @Autowired
    private ChatInfoMapper chatInfoMapper;

    @Autowired
    private ChatMessageMapper chatMessageMapper;

    @Autowired
    private MessageTemplate messageTemplate;
    private final ArkService service;

    @Autowired
    public FZChatMessageTemplateImpl(ArkService service) {
        // 在构造函数中初始化 ArkService
        this.service = service;
    }


    @Override
    public SseEmitter chatMessage(GeneralMessageDto dto, ModelList modelList) {
        UUID uuid = UUID.fastUUID();
        String content = dto.getPrompt();
        Integer messageId = dto.getParentMessageId();
        // String modelId = "deepseek-r1-250120";
        String modelId = modelList.getModelId();
        String sessionId = dto.getChatSessionId();

        log.info("SseEmitter 建立连接  uuid:{} 使用model：{}", uuid, modelId);

        // 对话历史
        List<ChatMessage> messageList = getHistoryMessageList(dto, 5);

        messageTemplate.saveMessage(Role.USER.getValue(), modelList, dto, null, null, content, MessageStatus.SUCCESS);

        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                // 指定您创建的方舟推理接入点 ID，此处已帮您修改为您的推理接入点 ID
                .model(modelId)
                .messages(messageList)
                .build();

        if (messageId == 1) {
            TaskVirtualExecutorUtil.executeWith(() -> {
                try {
                    ChatCompletionResult completion = service.createChatCompletion(ChatCompletionRequest.builder()
                            .model(modelId)
                            .messages(Collections.singletonList(ChatMessage.builder()
                                    .role(ChatMessageRole.SYSTEM)
                                    .content("请为下面这段对话生成一个简洁的标题 （不超过10个字!）：" + dto.getPrompt())
                                    .build()))
                            .build());

                    List<ChatCompletionChoice> choices = completion.getChoices();

                    String title;
                    if (null == choices || choices.isEmpty()) {
                        title = content.substring(0, 10);
                    } else {
                        title = (String) choices.get(0).getMessage().getContent();
                    }
                    chatInfoMapper.updateMessageTitle(sessionId, title);
                } catch (Exception e) {
                    log.info("获取文章标题错误  uuid:{}  error:{} ", uuid, e.getMessage());

                }
            });

        }


        // 消息持久化
        StringBuilder dbReasoningContent = new StringBuilder();
        StringBuilder dbContent = new StringBuilder();
        StringBuilder search = new StringBuilder();

        // 使用异步操作避免阻塞
        // try {
        SseEmitter emitter = null;
        try {
            Flowable<ChatCompletionChunk> chunkFlowable = service.streamChatCompletion(chatCompletionRequest);

            emitter = SseStreamHandler.<ChatCompletionChunk>builder()
                    .uuid(uuid)
                    .flowable(chunkFlowable)
                    .contentMapper(chunk -> {
                        if (chunk.getChoices().isEmpty()) return Optional.empty();
                        String reasoningContent = chunk.getChoices().get(0).getMessage().getReasoningContent();
                        ObjectNode objectNode = new ObjectMapper().createObjectNode();

                        if (StringUtils.isNotBlank(reasoningContent)) {
                            dbReasoningContent.append(reasoningContent);
                            objectNode.put("r", reasoningContent);
                        } else {
                            String pushContent = (String) chunk.getChoices().get(0).getMessage().getContent();
                            // Object pushContent = chunk.getChoices().get(0).getMessage().getContent();
                            if (!pushContent.isEmpty()) {
                                dbContent.append(pushContent);
                                objectNode.put("v", pushContent);
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
        } catch (Exception e) {
            log.error("SSE流错误 uuid:{} error:{}", uuid, e.getMessage());
            messageTemplate.saveMessage(Role.ASSISTANT.getValue(), modelList, dto, search.toString(), dbReasoningContent.toString(), dbContent.toString(), MessageStatus.FAILED);
        }


       /*     chunkFlowable.doOnError(error -> {
                        log.error("streamChatCompletion uuid:{} 意外出错:{} ", uuid, error.getMessage());
                        try {
                            emitter.send("Error occurred: " + error.getMessage(), MediaType.TEXT_EVENT_STREAM);
                        } catch (IOException e) {
                            emitter.completeWithError(e);
                            log.error("emitter send uuid:{} error:{} ", uuid, e.getMessage());
                        }
                    })
                    .flatMap(delta -> {
                        if (!delta.getChoices().isEmpty()) {
                            String reasoningContent = delta.getChoices().get(0).getMessage().getReasoningContent();
                            ObjectNode objectNode = new ObjectMapper().createObjectNode();

                            if (StringUtils.isNotBlank(reasoningContent)) {
                                dbReasoningContent.append(reasoningContent);
                                objectNode.put("r", reasoningContent);
                            } else {
                                String pushContent = (String) delta.getChoices().get(0).getMessage().getContent();
                                objectNode.put("v", pushContent);
                                dbContent.append(pushContent);
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
*/
           /* emitter.onCompletion(() -> {
                log.info("思考内容：{}", dbReasoningContent);
                log.info("内容：{}", dbContent);
                serveDBMessage(dbReasoningContent, dbContent);
                log.info("SSE流已完成 uuid:{}", uuid);
            });

            emitter.onError(e -> {
                emitter.completeWithError(e);
                log.error("SSE流发生错误 uuid:{} error:{}", uuid, e.getMessage());
            });*/
        // } catch (Exception e) {
        //     emitter.completeWithError(e);
        //     log.error("终止或网络异常 uuid:{} error:{}", uuid, e.getMessage());
        // }


        return emitter;
    }

    private void serveDBMessage(StringBuilder dbReasoningContent, StringBuilder dbContent) {

    }

    private void addStreamMessages(List<ChatMessage> streamMessages, String content) {
        ChatMessage userMessage = ChatMessage.builder()
                .role(ChatMessageRole.USER) // 设置消息角色为用户
                .content(content) // 设置消息内容
                .build();

        // 将用户消息添加到消息列表
        streamMessages.add(userMessage);
    }

    private List<ChatMessage> getHistoryMessageList(GeneralMessageDto dto, int size) {
        String sessionId = dto.getChatSessionId();
        List<AiMg> messageList = messageTemplate.getOrderedMessages(sessionId, size);

        List<ChatMessage> messages = null;
        try {
            messages = messageList.stream()
                    .map(chatMessage -> ChatMessage.builder()
                            .role(ChatMessageRole.valueOf(chatMessage.getRole().toUpperCase()))
                            .content(chatMessage.getContent())
                            .build())
                    .collect(Collectors.toCollection(ArrayList::new));

            messages.add(ChatMessage.builder()
                    .role(ChatMessageRole.USER)
                    .content(dto.getPrompt())
                    .build());
        } catch (Exception e) {
            log.error("历史记录转换错误 ：{} ", e.getMessage());
        }


        return messages;
    }
}
