package com.vk.ai.controller;// dashscope SDK的版本 >= 2.18.2

import com.alibaba.dashscope.aigc.generation.*;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import io.reactivex.Flowable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static StringBuilder reasoningContent = new StringBuilder();
    private static StringBuilder finalContent = new StringBuilder();
    private static boolean isFirstPrint = true;


    public static void main(String[] args) {
        SearchOptions searchOptions = SearchOptions.builder()
                // 使返回结果中包含搜索信息的来源
                .enableSource(false)
                // 强制开启互联网搜索
                .forcedSearch(false)
                // 开启角标标注
                .enableCitation(true)
                // 设置角标标注样式为[ref_i]
                .citationFormat("[ref_<number>]")
                // 联网搜索10条信息
                .searchStrategy("pro")
                .build();

        try {
            Generation gen = new Generation();
            Message userMsg = Message.builder().role(Role.USER.getValue()).content("哪吒2的票房？").build();
            GenerationParam param = GenerationParam.builder()
                    // 若没有配置环境变量，请用百炼API Key将下行替换为：.apiKey("sk-xxx")
                    .apiKey("sk-aff40a9d4b8a414ea461237d1fb6adc8")
                    // 此处以 qwq-plus 为例，可按需更换模型名称
                    .model("qwq-plus")
                    .messages(Arrays.asList(userMsg))
                    // 开启联网搜索的参数
                    .enableSearch(false)
                    // 开启流示输出
                    .incrementalOutput(true)
                    .searchOptions(searchOptions)
                    .build();

            Flowable<GenerationResult> result = gen.streamCall(param);

            result.blockingForEach(message -> {
                String reasoning = message.getOutput().getChoices().get(0).getMessage().getReasoningContent();
                String content = message.getOutput().getChoices().get(0).getMessage().getContent();
                SearchInfo searchInfo = message.getOutput().getSearchInfo();
                if (!reasoning.isEmpty()) {
                    reasoningContent.append(reasoning);
                    if (isFirstPrint) {
                        System.out.println("====================搜索信息====================");
                        isFirstPrint = false;
                        System.out.print(searchInfo.getSearchResults());

                        System.out.println("\n====================思考过程====================");
                    }
                    System.out.print(reasoning);
                }

                if (!content.isEmpty()) {
                    finalContent.append(content);
                    if (!isFirstPrint) {
                        System.out.println("\n====================完整回复====================");
                        isFirstPrint = true;
                    }
                    System.out.print(content);
                }
                if (message.getOutput().getChoices().get(0).getFinishReason().equals("stop") || message.getOutput().getChoices().get(0).getFinishReason().equals("length")) {
                    System.out.println("\n====================Token 消耗====================");
                    System.out.println(message.getUsage());
                }
            });

        } catch (ApiException | NoApiKeyException | InputRequiredException e) {
            logger.error("An exception occurred: {}", e.getMessage());
        }

    }
}