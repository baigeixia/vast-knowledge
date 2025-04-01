package com.vk.ai.controller;

import com.volcengine.ark.runtime.model.completion.chat.ChatCompletionContentPart;
import com.volcengine.ark.runtime.model.completion.chat.ChatCompletionRequest;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessage;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessageRole;
import com.volcengine.ark.runtime.service.ArkService;
import okhttp3.ConnectionPool;
import okhttp3.Dispatcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
        // final List messages = new ArrayList<>();
        // final List multiParts = new ArrayList<>();
        //
        // multiParts.add(ChatCompletionContentPart.builder().type("text").text(
        //         "这是哪里？"
        // ).build());
        // multiParts.add(ChatCompletionContentPart.builder().type("image_url").imageUrl(
        //         new ChatCompletionContentPart.ChatCompletionContentPartImageURL(
        //                 "https://ark-project.tos-cn-beijing.volces.com/images/view.jpeg"
        //         )
        // ).build());
        // multiParts.add(ChatCompletionContentPart.builder().type("text").text(
        //         "这是北京吗"
        // ).build());
        // multiParts.add(ChatCompletionContentPart.builder().type("text").text(
        //         "你第二个回复是什么"
        // ).build());

        // amultiParts.add(ChatCompletionContentPart.builder().type("text").text(
        //         "仅从这张图片来看，很难确切判断这是哪里。画面中展现了一个人在湖面上划着皮划艇，远处是茂密的森林和巍峨的雪山，整体景色优美，可能是世界上很多高山湖泊地区的景色，比如阿尔卑斯山区、落基山脉周边的一些湖泊地带等。你可以提供更多相关信息，以便更准确探讨地点。\n"
        // ).build());
        // ChatCompletionContentPart imageUrl = ChatCompletionContentPart.builder().type("image_url").imageUrl(
        //         new ChatCompletionContentPart.ChatCompletionContentPartImageURL(
        //                 "https://ark-project.tos-cn-beijing.volces.com/images/view.jpeg"
        //         )).build();
        // final ChatMessage userMessage =

        // final ChatMessage aMessage = ChatMessage.builder()
        //         .role(ChatMessageRole.ASSISTANT)
        //         .multiContent(amultiParts)
        //         .build();

        // messages.add(userMessage);

        List<ChatMessage> messagess = Arrays.asList(
                ChatMessage.builder()
                        .role(ChatMessageRole.USER)
                        .multiContent(Arrays.asList(
                                ChatCompletionContentPart.builder().type("text").text(
                                        "这是哪里？"
                                ).build(),
                                ChatCompletionContentPart.builder().type("image_url").imageUrl(
                                        new ChatCompletionContentPart.ChatCompletionContentPartImageURL(
                                                "https://ark-project.tos-cn-beijing.volces.com/images/view.jpeg"
                                        )).build()
                        )).build(),
                ChatMessage.builder().role(ChatMessageRole.ASSISTANT).content("这是一个湖").build(),
                ChatMessage.builder().role(ChatMessageRole.USER).content("我知道这是一个湖 这是北京吗 并且这是什么湖").build()
        );

        // messages.add(aMessage);


        // List<ChatMessage> messagess = Arrays.asList(
        //         ChatMessage.builder().role(ChatMessageRole.SYSTEM).content("你是豆包，是由字节跳动开发的 AI 人工智能助手").build(),
        //         ChatMessage.builder().role(ChatMessageRole.USER).content("你可以做什么").build(),
        //         ChatMessage.builder().role(ChatMessageRole.ASSISTANT).content("我可以做很多事情，以下是一些主要方面：\n" +
        //                 "\n" +
        //                 "### 知识问答类\n" +
        //                 "- **解答各种常识问题**：无论是历史、地理、科学（物理、化学、生物等）、文化、艺术等领域的常见知识疑问，比如“第一次世界大战爆发的原因是什么”“珠穆朗玛峰的高度是多少”等，都能为你详细解答。\n" +
        //                 "- **专业领域初步知识讲解**：对于一些相对专业的领域，如计算机科学、医学、经济学等，能提供基础概念和常见问题的解释，例如“什么是区块链技术”“通货膨胀对经济有哪些影响” 。\n" +
        //                 "\n" +
        //                 "### 文本创作类\n" +
        //                 "- **写作文章**：可以撰写各类体裁的文章，像记叙文（如记录一次难忘的旅行经历）、议论文（针对某个社会现象发表观点和论证）、说明文（介绍一种产品或技术），以及诗歌、小说片段等。\n" +
        //                 "- **文案创作**：包括商业文案（如广告文案、产品宣传文案）、社交媒体文案（有趣的朋友圈文案、微博文案）、求职相关文案（简历描述、求职信）等。\n" +
        //                 "\n" +
        //                 "### 语言学习类\n" +
        //                 "- **语法讲解**：对各种语言（目前主要以中文和英文为主）的语法规则进行详细解释，比如中文的句式结构、英文的时态用法等。\n" +
        //                 "- **翻译服务**：进行中英文等语言之间的文本翻译，无论是单词、短语、句子还是段落等。\n" +
        //                 "- **口语练习辅助**：提供口语表达的建议、纠正发音问题（虽然无法直接进行语音交互，但可以用文字形式指导），分享一些常用口语表达和场景对话。\n" +
        //                 "\n" +
        //                 "### 生活建议类\n" +
        //                 "- **日常事务规划**：例如制定旅行计划（包括行程安排、景点推荐、交通住宿规划等）、活动策划（生日派对、团队建设活动等）。\n" +
        //                 "- **情感问题疏导**：倾听你在人际关系、情感困惑等方面的烦恼，并给出分析和合理的建议，帮你缓解情绪、理清思路。\n" +
        //                 "- **健康生活指导**：提供饮食、运动、心理健康等方面的一般性建议，比如“如何制定健康的减肥食谱”“怎样缓解工作压力” 。\n" +
        //                 "\n" +
        //                 "### 创意启发类\n" +
        //                 "- **头脑风暴支持**：在你进行创意构思时，如设计新产品、创作新故事等，提供各种创意想法和角度，激发你的灵感。\n" +
        //                 "- **游戏互动**：陪你玩猜谜、成语接龙、故事接龙等文字类游戏。").build(),
        //         ChatMessage.builder().role(ChatMessageRole.USER).content("你可以写一个清明的诗吗").build(),
        //         ChatMessage.builder().role(ChatMessageRole.ASSISTANT).content("# 清明寄思\n" +
        //                 "清明时节雨如弦，烟柳含悲笼野田。\n" +
        //                 "碑上苔痕凝旧忆，坟前菊蕊寄新牵。\n" +
        //                 "遥思亲影情难已，每念音容泪泫然。\n" +
        //                 "风抚松楸云黯黯，心香一炷祭流年。 ").build(),
        //         ChatMessage.builder().role(ChatMessageRole.USER).content("坟前菊蕊寄新牵  坟前改为碑前").build(),
        //         ChatMessage.builder().role(ChatMessageRole.ASSISTANT).content("# 清明寄思\n" +
        //                 "清明时节雨如弦，烟柳含悲笼野田。\n" +
        //                 "碑上苔痕凝旧忆，碑前菊蕊寄新牵。\n" +
        //                 "遥思亲影情难已，每念音容泪泫然。\n" +
        //                 "风抚松楸云黯黯，心香一炷祭流年。  ").build(),
        //         ChatMessage.builder().role(ChatMessageRole.USER).content("根据上面的诗在丰富下感情").build()
        //
        // );

        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                // 指定您创建的方舟推理接入点 ID，此处已帮您修改为您的推理接入点 ID
                .model("doubao-1-5-vision-pro-32k-250115")
                .messages(messagess)
                .build();

        service.streamChatCompletion(chatCompletionRequest)
                .doOnError(Throwable::printStackTrace)
                .blockingForEach(
                        choice -> {
                            if (!choice.getChoices().isEmpty()) {
                                System.out.print(choice.getChoices().get(0).getMessage().getContent());
                            }
                        }
                );

        service.shutdownExecutor();
    }
}