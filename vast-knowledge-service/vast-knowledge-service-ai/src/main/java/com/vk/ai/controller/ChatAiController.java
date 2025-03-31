package com.vk.ai.controller;


import com.vk.ai.config.ArkConfig;
import com.vk.ai.domain.dto.MessageDto;
import com.vk.common.core.exception.LeadNewsException;
import com.vk.common.core.utils.StringUtils;
import com.volcengine.ark.runtime.model.completion.chat.ChatCompletionRequest;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessage;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessageRole;
import com.volcengine.ark.runtime.service.ArkService;
import okhttp3.ConnectionPool;
import okhttp3.Dispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/chat")
public class ChatAiController {

    private final ArkConfig arkConfig;
    private final ArkService arkService;

    @Autowired
    public ChatAiController(ArkConfig arkConfig) {
        this.arkConfig = arkConfig;

        // 在构造函数中初始化 ArkService
        this.arkService = ArkService.builder()
                .timeout(Duration.ofSeconds(1800))
                .connectTimeout(Duration.ofSeconds(20))
                .dispatcher(new Dispatcher())
                .connectionPool(new ConnectionPool(5, 1, TimeUnit.SECONDS))
                .baseUrl("https://ark.cn-beijing.volces.com/api/v3")
                .apiKey(arkConfig.getApiKey())
                .build();
    }

    @PostMapping("/stream")
    public Mono<ResponseEntity<String>> streamChat(
            @RequestBody MessageDto message
    ) {
        if (null == message){
            throw new LeadNewsException("消息不能为空");
        }
        String text = message.getText();
        String model = message.getModel();

        List<ChatMessage> streamMessages = new ArrayList<>();
        ChatMessage userMessage = ChatMessage.builder()
                .role(ChatMessageRole.USER) // 设置消息角色为用户
                .content(text) // 设置消息内容
                .build();

        // 将用户消息添加到消息列表
        streamMessages.add(userMessage);

        ChatCompletionRequest streamChatCompletionRequest = ChatCompletionRequest.builder()
                .model(StringUtils.isEmpty(model) ? "deepseek-r1-250120" : model)
                .messages(streamMessages)
                .build();


        StringBuilder output = new StringBuilder();

        arkService.streamChatCompletion(streamChatCompletionRequest)
                .doOnError(Throwable::printStackTrace)
                .blockingForEach(delta -> {
                    if (!delta.getChoices().isEmpty()) {
                        String content = StringUtils.isNotEmpty(delta.getChoices().get(0).getMessage().getReasoningContent()) ?
                                delta.getChoices().get(0).getMessage().getReasoningContent() :
                                delta.getChoices().get(0).getMessage().getContent().toString();
                        output.append(content);
                    }
                });

        return Mono.just(ResponseEntity.ok(output.toString()));
    }
}
