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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/chat")
public class ChatAiController {

    private final ArkService service;

    @Autowired
    public ChatAiController(ArkService service) {
        // 在构造函数中初始化 ArkService
        this.service = service;
    }

    @PostMapping("/stream")
    public Mono<ResponseEntity<String>> streamChat(
            @RequestBody MessageDto message
    ) {
        if (null == message) {
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

//        ChatCompletionRequest streamChatCompletionRequest = ChatCompletionRequest.builder()
//                .model("deepseek-r1-250120")
//                .messages(streamMessages)
//                .build();

        service.streamChatCompletion(streamChatCompletionRequest)
                .doOnError(Throwable::printStackTrace)
                .blockingForEach(
                        delta -> {
                            if (!delta.getChoices().isEmpty()) {
                                if (StringUtils.isNotEmpty(delta.getChoices().get(0).getMessage().getReasoningContent())) {
                                    System.out.print(delta.getChoices().get(0).getMessage().getReasoningContent());
                                } else {
                                    System.out.print(delta.getChoices().get(0).getMessage().getContent());
                                }
                            }
                        }
                );


        return Mono.just(ResponseEntity.ok(output.toString()));
    }

    @GetMapping(value = "/stream-chat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamChat(@RequestParam(name = "message") String message) {
        SseEmitter emitter = new SseEmitter(60_000L); // 设置1分钟超时

        List<ChatMessage> streamMessages = new ArrayList<>();

        ChatMessage userMessage = ChatMessage.builder()
                .role(ChatMessageRole.USER) // 设置消息角色为用户
                .content(message) // 设置消息内容
                .build();

        // 将用户消息添加到消息列表
        streamMessages.add(userMessage);


        ChatCompletionRequest streamChatCompletionRequest = ChatCompletionRequest.builder()
                .model( "deepseek-r1-250120" )
                .messages(streamMessages)
                .stream(true)
                .streamOptions(ChatCompletionRequest.ChatCompletionRequestStreamOptions.of(true))
                .build();

        // 使用异步操作避免阻塞
        // 确保流完成时关闭连接
        try {
            service.streamChatCompletion(streamChatCompletionRequest)
                    .doOnError(Throwable::printStackTrace)
                    .flatMap(delta -> {
                        if (!delta.getChoices().isEmpty()) {
                            String reasoningContent = delta.getChoices().get(0).getMessage().getReasoningContent();
                            String content = StringUtils.isNotEmpty(reasoningContent) ? reasoningContent : (String) delta.getChoices().get(0).getMessage().getContent();

                            return Mono.just(content); // 包装内容为Mono
                        }
                        return Mono.empty(); // 没有内容时返回空
                    })
                    .doOnNext(content -> {
                        try {
                            emitter.send(content, MediaType.TEXT_PLAIN); // 发送流数据
                        } catch (IOException e) {
                            emitter.completeWithError(e); // 如果发生错误，完成事件
                        }
                    })
                    .doOnTerminate(emitter::complete)
                    .subscribe(); // 异步执行流操作

            // 处理完毕后返回
            emitter.onCompletion(() -> {
                System.out.println("SSE流已完成");
            });

            emitter.onError(e -> {
                System.out.println("SSE流发生错误：" + e.getMessage());
                emitter.completeWithError(e); // 错误发生时关闭连接
            });
        } catch (Exception e) {
            // throw new RuntimeException(e);
            System.out.println("终止或网络异常"+ e.getMessage());
        }

        return emitter;
    }




}
