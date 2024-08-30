package com.vk.behaviour.domain.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 私信消息
 */
@Data
public class ChatMsgDto implements Serializable {
    /**
     * 对方用户id
     */
    private  Long otherSideUserId;
    /**
     * 私信内容
     */
    private  String msg;
    /**
     * 私信图片内容
     */
    private  String image;
}
