package com.vk.common.mq.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewMsg {
    /**
     * 用户id
     */
    private  Long userId;
    /**
     * 发送人 id
     */
    private  Long senderId;
    /**
     * 发送人 名称
     */
    private  String senderName;
    /**
     * 内容
     */
    private  String content;
    /**
     * 消息类型
     */
    private  Integer type;
    /**
     * 是否阅读
     */
    private  Integer isRead;
    /**
     * 创造时间
     */
    private  LocalDateTime createdTime;
    /**
     * 阅读时间
     */
    private  LocalDateTime readTime;
}
