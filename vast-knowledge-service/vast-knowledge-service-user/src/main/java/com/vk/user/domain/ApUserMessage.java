package com.vk.user.domain;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.keygen.KeyGenerators;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import java.time.LocalDateTime;

/**
 * APP用户消息通知信息 实体类。
 *
 * @author 张三
 * @since 2024-05-13
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "ap_user_message")
public class ApUserMessage implements Serializable {

    /**
     * 主键
     */
    @Id(keyType=KeyType.Generator, value= KeyGenerators.flexId)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 发送人ID
     */
    private Long senderId;

    /**
     * 发送人昵称
     */
    private String senderName;

    /**
     * 私信内容
     */
    private String content;

    /**
     * 消息类型
     * 0:关注 1:取消关注 2:点赞文章 3:取消点赞文章 4:转发文章 5:收藏文章 6:点赞评论  7:取消评论点赞 8:审核通过评论 9:私信通知 10:评论通知 11:分享通知
     * 100:身份证审核通过 101:身份证审核拒绝 102:实名认证通过 103:实名认证失败 104:自媒体人祝贺 105:异常登录通知 106:反馈回复 107:转发通知
     */
    private Integer type;

    /**
     * 是否阅读
     */
    private Integer isRead;

    /**
     * 创建时间
     */
    private LocalDateTime createdTime;

    /**
     * 阅读时间
     */
    private LocalDateTime readTime;

}
