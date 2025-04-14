package com.vk.ai.controller;


import com.alibaba.fastjson2.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/chat")
@CrossOrigin
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
            StringBuilder sb = new StringBuilder();
            service.streamChatCompletion(streamChatCompletionRequest)
                    .doOnError(Throwable::printStackTrace)
                    .flatMap(delta -> {
                        if (!delta.getChoices().isEmpty()) {
                            String reasoningContent = delta.getChoices().get(0).getMessage().getReasoningContent();
                            String content = StringUtils.isNotEmpty(reasoningContent) ? reasoningContent : (String) delta.getChoices().get(0).getMessage().getContent();
                            sb.append(content);
                            return Mono.just(content); // 包装内容为Mono
                        }
                        return Mono.empty(); // 没有内容时返回空
                    })
                    .doOnNext(content -> {
                        try {
                            // emitter.send(content, MediaType.TEXT_PLAIN); // 发送流数据
                            emitter.send(content, MediaType.TEXT_EVENT_STREAM); // 发送流数据
                        } catch (IOException e) {
                            emitter.completeWithError(e); // 如果发生错误，完成事件
                        }
                    })
                    .doOnTerminate(emitter::complete)
                    .subscribe(); // 异步执行流操作



            // 处理完毕后返回
            emitter.onCompletion(() -> {
                System.out.println(sb);
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


    @GetMapping(value = "/stream-chat2", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamChat2(@RequestParam(name = "message") String message) {
        SseEmitter emitter = new SseEmitter(60_000L); // 设置1分钟超时

        List<ChatMessage> streamMessages = new ArrayList<>();

        ChatMessage userMessage = ChatMessage.builder()
                .role(ChatMessageRole.USER) // 设置消息角色为用户
                .content(message) // 设置消息内容
                .build();

        // 将用户消息添加到消息列表
        streamMessages.add(userMessage);


        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                // 指定您创建的方舟推理接入点 ID，此处已帮您修改为您的推理接入点 ID
                // .model("doubao-1-5-vision-pro-32k-250115")
                .model("deepseek-r1-250120")
                .messages(streamMessages)
                .build();


        // ChatCompletionRequest streamChatCompletionRequest = ChatCompletionRequest.builder()
        //         .model( "doubao-1-5-vision-pro-32k-250115" )
        //         .messages(streamMessages)
        //         .stream(true)
        //         .streamOptions(ChatCompletionRequest.ChatCompletionRequestStreamOptions.of(true))
        //         .build();

        // 使用异步操作避免阻塞
        // 确保流完成时关闭连接
        try {
            StringBuilder sb = new StringBuilder();
            service.streamChatCompletion(chatCompletionRequest)
                    .doOnError(error -> {
                        error.printStackTrace();
                        try {
                            emitter.send("Error occurred: " + error.getMessage(), MediaType.TEXT_EVENT_STREAM);
                        } catch (IOException e) {
                            emitter.completeWithError(e);
                        }
                    })
                    .flatMap(delta -> {
                        if (!delta.getChoices().isEmpty()) {
                            String reasoningContent = delta.getChoices().get(0).getMessage().getReasoningContent();
                            ObjectMapper mapper = new ObjectMapper();
                            ObjectNode objectNode = mapper.createObjectNode();

                            if (StringUtils.isNotEmpty(reasoningContent)) {
                                objectNode.put("r", reasoningContent);
                            }else {
                                String content = (String)delta.getChoices().get(0).getMessage().getContent();
                                objectNode.put("v", content);
                            }
                            return Mono.just(objectNode.toString()); // 包装内容为Mono
                        }
                        return Mono.empty(); // 没有内容时返回空
                    })
                    .doOnNext(content -> {
                        try {
                            emitter.send(content, MediaType.TEXT_EVENT_STREAM); // 发送流数据
                        } catch (IOException e) {
                            emitter.completeWithError(e); // 如果发生错误，完成事件
                        }
                    })
                    .doOnTerminate(emitter::complete)
                    .subscribe(); // 异步执行流操作



            // 处理完毕后返回
            emitter.onCompletion(() -> {
                System.out.println(sb);
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
