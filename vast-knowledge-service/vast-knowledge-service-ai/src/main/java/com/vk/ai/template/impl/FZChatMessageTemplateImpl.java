package com.vk.ai.template.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.vk.ai.domain.ModelList;
import com.vk.ai.domain.dto.GeneralMessageDto;
import com.vk.ai.enums.AiType;
import com.vk.ai.mapper.ChatInfoMapper;
import com.vk.ai.mapper.ChatMessageMapper;
import com.vk.ai.template.AbstractAiTemplate;
import com.vk.common.core.utils.StringUtils;
import com.vk.common.core.utils.uuid.UUID;
import com.volcengine.ark.runtime.model.completion.chat.ChatCompletionRequest;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessage;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessageRole;
import com.volcengine.ark.runtime.service.ArkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    private final ArkService service;

    @Autowired
    public FZChatMessageTemplateImpl(ArkService service) {
        // 在构造函数中初始化 ArkService
        this.service = service;
    }


    @Override
    public SseEmitter chatMessage(GeneralMessageDto message, ModelList modelList) {
        UUID uuid = UUID.fastUUID();


        String content = message.getPrompt();
        String modelId = "deepseek-r1-250120";


        SseEmitter emitter = new SseEmitter(60_000L); // 设置1分钟超时

        log.info("SseEmitter 建立连接  uuid:{} 使用model：{}",uuid,modelId);

        List<ChatMessage> streamMessages = new ArrayList<>();

        // 添加上下文
        addStreamMessages(streamMessages, content);

        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                // 指定您创建的方舟推理接入点 ID，此处已帮您修改为您的推理接入点 ID
                .model(modelId)
                .messages(streamMessages)
                .build();

        //消息持久化
        StringBuilder dbReasoningContent = new StringBuilder();
        StringBuilder dbContent = new StringBuilder();

        // 使用异步操作避免阻塞
        try {
            service.streamChatCompletion(chatCompletionRequest)
                    .doOnError(error -> {
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

            emitter.onCompletion(() -> {
                log.info("思考内容：{}", dbReasoningContent);
                log.info("内容：{}", dbContent);
                serveDBMessage(dbReasoningContent,dbContent);
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

    private void serveDBMessage(StringBuilder dbReasoningContent, StringBuilder dbContent) {

    }

    private void addStreamMessages(List<ChatMessage> streamMessages,String content) {
        ChatMessage userMessage = ChatMessage.builder()
                .role(ChatMessageRole.USER) // 设置消息角色为用户
                .content(content) // 设置消息内容
                .build();

        // 将用户消息添加到消息列表
        streamMessages.add(userMessage);
    }
}
