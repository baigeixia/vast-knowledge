package com.vk.behaviour.domain.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 私信消息
 */
@Data
public class CollectMsgDto implements Serializable {

    /**
     * 发送人ID
     */
    private  Long senderId;
    /**
     * 发送人名称
     */
    private  String senderName;

    /**
     * 文章ID
     */
    private Long articleId;



}
