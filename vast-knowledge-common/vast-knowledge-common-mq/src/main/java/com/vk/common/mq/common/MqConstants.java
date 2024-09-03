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
        String NEWS_COMMENT_TOPIC = "news.comment.topic";
    }



    interface UserSocketCS{
        /** 点赞或者取消点赞 通知
         * 1 评论  2 点赞   3 粉丝  4 关注  5 私信  6 系统消息
         */
        int NEW_COMMENT = 1;
        int NEW_LIKE = 2;
        int NEW_FAN = 3;
        int NEW_PRIVATE_MSG = 4;
        int NEW_CHAT_MSG = 5;
        int NEW_SYSTEM_MSG = 6;

    }

    interface SocketType{
        /**
         * 消息类型 0:关注 1:取消关注 2:点赞文章 3:取消点赞文章 4:转发文章 5:收藏文章 6:点赞评论  7:取消评论点赞 8:审核通过评论 9:私信通知 10:评论通知 11:分享通知
         * 100:身份证审核通过 101:身份证审核拒绝 102:实名认证通过 103:实名认证失败 104:自媒体人祝贺 105:异常登录通知 106:反馈回复 107:转发通知
         */
        int FOLLOW = 0;
        int FOLLOW_NO = 1;
        int LIKE = 2;
        int LIKE_NO = 3;
        int FORWARD = 4;
        int COLLECT = 5;
        int LIKE_COMMENT = 6;
        int LIKE_COMMENT_NO = 7;
        int APPROVED = 8;
        int CHAT_MSG = 9;
        int COMMENT = 10;
        int SHARE = 11;

        /**
         * 点赞类型  0 文章 1 评论
         */
        int LIKE_TYPE_ARTICLE= 0;
        int LIKE_TYPE_ARTICLE_NO= 1;
    }


}
