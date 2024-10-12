package com.vk.behaviour.domain.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
public class CommentMsg extends  BaseMsgDto{

    /**
     * 文章ID
     */
    private Long articleId;

    /**
     * 发送人ID
     */
    private  Long senderId;
    // /**
    //  * 文章作者
    //  */
    // private  Long articleAuthor;
    /**
     * 发送人名称
     */
    private  String senderName;



}
