package com.vk.common.mq.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewUserMsg {
    /**
     * 用户id
     */
    private  Long userId;
    /**
     * 用户名称
     */
    private  String userName;
    /**
     * 要发送的 id
     */
    private  Long senderId;
    /**
     * 要发送人 名称
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
}
