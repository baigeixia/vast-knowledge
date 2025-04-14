package com.vk.behaviour.stream;


import com.alibaba.fastjson2.JSON;
import com.vk.common.mq.common.MqConstants;
import com.vk.common.mq.domain.CommentVisitStreamMess;
import com.vk.common.mq.domain.NewCommentUpMsg;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Aggregator;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * @version 1.0
 * @description 说明
 * @package com.vk.article.stream
 */
@Configuration
public class HotCommentStreamHandler {

    @Bean
    public KStream<String, String> kCommentStream(StreamsBuilder streamsBuilder) {
        // 监听输入，创建流对象
        KStream<String, String> stream = streamsBuilder.stream(MqConstants.TopicCS.NEWS_COMMENT_TOPIC);
        // 流处理,
        // 消息 UpdateArticleMess
        // 聚合处理后的消息 ArticleVisitStreamMess
        stream.map((key, value) -> {
                    // value 消息 = UpdateArticleMess jsonString
                    // 解析json字符串得消息对象UpdateArticleMess
                    NewCommentUpMsg msg = JSON.parseObject(value, NewCommentUpMsg.class);
                    // 获取文章id
                    Long commentId = msg.getCommentId();
                    if (commentId == null) {
                        // 处理空值情况，例如使用默认值或者记录日志
                        commentId = -1L; // 使用默认值
                        // 可以记录日志
                        System.err.println("Warning: commentId is null for message: " + value);
                    }
                    return new KeyValue<>(
                            commentId.toString(), value
                    );
                })// 分组聚合，按key分组聚合，按文章的id聚合
                .groupBy((key, value) -> key)// 按key分组
                // 统计周期 每10秒统计一次
                .windowedBy(TimeWindows.of(Duration.ofSeconds(10)))
                // 把多个消息聚合成一个
                .aggregate(
                        // 初始化
                        () -> {
                            NewCommentUpMsg finalMsg = new NewCommentUpMsg();
                            return JSON.toJSONString(finalMsg);
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
                                CommentVisitStreamMess finalMsg = JSON.parseObject(finalMsgJSONString, CommentVisitStreamMess.class);
                                // 把消息内容 value 转成 UpdateArticleMess 点赞, commnt
                                NewCommentUpMsg msg = JSON.parseObject(value, NewCommentUpMsg.class);
                                // 设置文章 id
                                finalMsg.setCommentId(msg.getCommentId());
                                // 聚合 累加
                                finalMsg.setLike(finalMsg.getLike() + msg.getNum());
                                // 返回 最累计的结果
                                String result = JSON.toJSONString(finalMsg);
                                System.err.println(result);
                                return result;
                            }
                            // 取别名，唯一即可
                        }, Materialized.as("hotComment"))
                .toStream()
                // 转成kafka的消息数据格式
                .map((key, value) -> new KeyValue<>(key.key(), value))
                .to(MqConstants.TopicCS.NEWS_COMMENT_INCR_HANDLE_TOPIC);
        return stream;
    }
}
