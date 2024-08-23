package com.vk.common.mq.common;

public interface MqConstants {

    String NOTIFY_GROUP ="notify-group";
    interface TopicCS{
        /** 文章自动审核主的topic */
        String NEWS_ARTICLE_AUTO_SCAN_TOPIC = "news.article.auto.scan.topic";
        /** 上下架主题 */
        String NEWS_ARTICLE_STATE_SCAN_TOPIC = "news.article.state.up.topic";
        /** 用户行为 */
        String FOLLOW_BEHAVIOR_TOPIC="follow.behavior.topic";
        /** 用户行为 kafka stream 输入 topic */
        String  HOT_ARTICLE_SCORE_TOPIC="article_behavior_input";

        /** 用户行为 kafka stream 消费者 topic */
        String HOT_ARTICLE_INCR_HANDLE_TOPIC="article_behavior_out";

        String NEWS_LIKE_TOPIC = "news.like.topic";
    }



    interface UserSocketCS{
        /** 点赞通知 */
        String NEWS_LIKE = "newsLike";

    }


}
