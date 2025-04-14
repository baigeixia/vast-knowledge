package com.vk.ai.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.vk.common.core.utils.StringUtils;
import com.volcengine.ark.runtime.model.completion.chat.ChatCompletionContentPart;
import com.volcengine.ark.runtime.model.completion.chat.ChatCompletionRequest;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessage;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessageRole;
import com.volcengine.ark.runtime.service.ArkService;
import okhttp3.ConnectionPool;
import okhttp3.Dispatcher;
import org.springframework.http.MediaType;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


// 请确保您已将 API Key 存储在环境变量 ARK_API_KEY 中
// 初始化Ark客户端，从环境变量中读取您的API Key
public class ChatCompletionsVisionExample {
    // 从环境变量中获取您的 API Key。此为默认方式，您可根据需要进行修改
    static String apiKey = "50448dfc-b8e4-4715-99d5-f936a34b0fb2";
    // 此为默认路径，您可根据业务所在地域进行配置
    static String baseUrl = "https://ark.cn-beijing.volces.com/api/v3";
    static ConnectionPool connectionPool = new ConnectionPool(5, 1, TimeUnit.SECONDS);
    static Dispatcher dispatcher = new Dispatcher();
    static ArkService service = ArkService.builder().dispatcher(dispatcher).connectionPool(connectionPool).baseUrl(baseUrl).apiKey(apiKey).build();

    public static void main(String[] args) {
        System.out.println("----- image input -----");
        final List messages = new ArrayList<>();
        final List multiParts = new ArrayList<>();
        multiParts.add(ChatCompletionContentPart.builder().type("text").text(
                "你是谁"
        ).build());
        final ChatMessage userMessage = ChatMessage.builder().role(ChatMessageRole.USER)
                .multiContent(multiParts).build();
        messages.add(userMessage);

        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                // 指定您创建的方舟推理接入点 ID，此处已帮您修改为您的推理接入点 ID
                // .model("doubao-1-5-vision-pro-32k-250115")
                .model("deepseek-r1-250120")
                .messages(messages)
                .build();

        service.streamChatCompletion(chatCompletionRequest)
                .doOnError(Throwable::printStackTrace)
                .flatMap(delta -> {
                    if (!delta.getChoices().isEmpty()) {
                        System.out.println(delta.getChoices().get(0).toString());
                        ObjectMapper mapper = new ObjectMapper();
                        ObjectNode objectNode = mapper.createObjectNode();

                        String reasoningContent = delta.getChoices().get(0).getMessage().getReasoningContent();
                        String content = StringUtils.isNotEmpty(reasoningContent) ? reasoningContent : (String) delta.getChoices().get(0).getMessage().getContent();

                        objectNode.put("v",content);
                        return Mono.just(objectNode.toString());
                    }
                    return Mono.empty(); // 没有内容时返回空
                })
                .doOnNext(System.out::println)
                .subscribe(); // 异步执行流操作



        service.shutdownExecutor();
    }
}