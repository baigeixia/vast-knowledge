package com.vk.ai.controller;

import com.volcengine.ark.runtime.model.completion.chat.ChatCompletionRequest;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessage;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessageRole;
import com.volcengine.ark.runtime.service.ArkService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class StreamChatCompletionsExample {
    public static void main(String[] args) {
        String apiKey = "50448dfc-b8e4-4715-99d5-f936a34b0fb2";
        ArkService service = ArkService.builder().apiKey(apiKey).build();
        String baseUrl = "https://ark.cn-beijing.volces.com/api/v3";
        System.out.println("\n----- streaming request -----");
        final List<ChatMessage> messages = Arrays.asList(
                ChatMessage.builder().role(ChatMessageRole.SYSTEM).content("你是豆包，是由字节跳动开发的 AI 人工智能助手").build(),
                ChatMessage.builder().role(ChatMessageRole.USER).content("花椰菜是什么？").build(),
                ChatMessage.builder().role(ChatMessageRole.ASSISTANT).content("花椰菜又称菜花、花菜，是一种常见的蔬菜。").build(),
                ChatMessage.builder().role(ChatMessageRole.USER).content("再详细点").build()
        );

        ChatCompletionRequest streamChatCompletionRequest = ChatCompletionRequest.builder()
                .model("doubao-1-5-vision-pro-32k-250115")
                .messages(messages)
                .build();

        // service.createChatCompletion(streamChatCompletionRequest).getChoices().forEach(choice -> System.out.println(choice.getMessage().getContent()));

        service.streamChatCompletion(streamChatCompletionRequest)
                .doOnError(Throwable::printStackTrace)
                .blockingForEach(
                        choice -> {
                            if (!choice.getChoices().isEmpty()) {
                                System.out.print(choice.getChoices().get(0).getMessage().getContent());
                            }
                        }
                );

        // shutdown service
        service.shutdownExecutor();
    }

}
