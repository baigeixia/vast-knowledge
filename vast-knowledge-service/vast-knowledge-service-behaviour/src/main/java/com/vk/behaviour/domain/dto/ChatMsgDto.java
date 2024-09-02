package com.vk.behaviour.domain.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 私信消息
 */
@Data
public class ChatMsgDto implements Serializable {
    /**
     * 用户id
     */
    private  Long userId;
    /**
     * 发送人ID
     */
    private  Long senderId;
    /**
     * 发送人昵称
     */
    private  String senderName;

    /**
     * 私信内容
     */
    private  String content;
    /**
     * 是否阅读
     */
    private  Integer isRead;
    /**
     * 创建时间
     */
    private  LocalDateTime createdTime;

    /**
     * 阅读时间
     */
    private LocalDateTime readTime;
}
