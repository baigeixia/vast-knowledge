package com.vk.socket.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 私信消息
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ChatMsgDto extends  BaseMsgDto{
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
