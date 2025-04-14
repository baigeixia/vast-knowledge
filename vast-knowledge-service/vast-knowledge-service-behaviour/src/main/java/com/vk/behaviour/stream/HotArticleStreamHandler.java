package com.vk.behaviour.stream;


import com.alibaba.fastjson2.JSON;
import com.vk.common.mq.common.MqConstants;
import com.vk.common.mq.domain.ArticleVisitStreamMess;
import com.vk.common.mq.domain.UpdateArticleMess;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * @version 1.0
 * @description 说明
 * @package com.vk.article.stream
 */
@Configuration
public class HotArticleStreamHandler {

    @Bean
    public KStream<String, String> kStream(StreamsBuilder streamsBuilder) {
        // 监听输入，创建流对象
        KStream<String, String> stream = streamsBuilder.stream(MqConstants.TopicCS.HOT_ARTICLE_SCORE_TOPIC);
        // 流处理,
        // 消息 UpdateArticleMess
        // 聚合处理后的消息 ArticleVisitStreamMess
        stream.map((key, value) -> {
                    // value 消息 = UpdateArticleMess jsonString
                    // 解析json字符串得消息对象UpdateArticleMess
                    UpdateArticleMess msg = JSON.parseObject(value, UpdateArticleMess.class);
                    // 获取文章id
                    Long articleId = msg.getArticleId();
                    if (articleId == null) {
                        // 处理空值情况，例如使用默认值或者记录日志
                        articleId = -1L; // 使用默认值
                        // 可以记录日志
                        System.err.println("Warning: articleId is null for message: " + value);
                    }
                    return new KeyValue<>(
                            articleId.toString(), value
                    );
                })// 分组聚合，按key分组聚合，按文章的id聚合
                .groupBy((key, value) -> key)// 按key分组
                // 统计周期 每10秒统计一次
                .windowedBy(TimeWindows.of(Duration.ofSeconds(10)))
                // 把多个消息聚合成一个
                .aggregate(
                        // 初始化
                        new Initializer<String>() {
                            @Override
                            public String apply() {
                                ArticleVisitStreamMess finalMsg = new ArticleVisitStreamMess();
                                return JSON.toJSONString(finalMsg);
                            }
                        },
                        // 聚合器
                        new Aggregator<String, String, String>() {
                            /**
                             *
                             * @param key       the key of the record  文章id
                             * @param value     the value of the record UpdateArticleMess JSON字符串
                             * @param finalMsgJSONString the current aggregate value， 也是初始化器返回的数据
                             * @return the new aggregate value
                             */
                            @Override
                            // 方法每执行一次，代表处理一条消息, 并发了100条消息，执行100次， 变成一个消息
                            // 第二次调用时，finalMsgJSONString的内容为第一次计算后的结果
                            // 第三次调用时，finalMsgJSONString的内容为第二次计算后的结果
                            // 第n次，finalMsgJSONString的内容为第n-1次计算后的结果
                            public String apply(String key, String value, String finalMsgJSONString) {
                                // 累计
                                // 把初始化转成java对象 ArticleVisitStreamMess 最终要的消息 记录 +1 +1
                                ArticleVisitStreamMess finalMsg = JSON.parseObject(finalMsgJSONString, ArticleVisitStreamMess.class);
                                // 把消息内容 value 转成 UpdateArticleMess 点赞, commnt
                                UpdateArticleMess msg = JSON.parseObject(value, UpdateArticleMess.class);
                                // 设置文章 id
                                finalMsg.setArticleId(msg.getArticleId());
                                // 聚合 累加
                                switch (msg.getType().getValue()) {
                                    // COLLECTION(1),COMMENT(2),LIKES(3),VIEWS(4);
                                    // 累加收藏数量
                                    case 1:
                                        finalMsg.setCollect(finalMsg.getCollect() + msg.getNum());
                                        break;
                                    case 2:
                                        finalMsg.setComment(finalMsg.getComment() + msg.getNum());
                                        break;
                                    case 3:
                                        finalMsg.setLike(finalMsg.getLike() + msg.getNum());
                                        break;
                                    case 4:
                                        finalMsg.setView(finalMsg.getView() + msg.getNum());
                                        break;
                                }
                                // 返回 最累计的结果
                                String result = JSON.toJSONString(finalMsg);
                                System.err.println(result);
                                return result;
                            }
                            // 取别名，唯一即可
                        }, Materialized.as("hotArticle"))
                .toStream()
                // 转成kafka的消息数据格式
                .map((key, value) -> new KeyValue<>(key.key(), value))
                .to(MqConstants.TopicCS.HOT_ARTICLE_INCR_HANDLE_TOPIC);
        return stream;
    }
}
