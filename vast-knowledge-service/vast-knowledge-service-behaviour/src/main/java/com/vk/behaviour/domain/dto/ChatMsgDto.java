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
     * 图片
     */
    private  String image;

}
