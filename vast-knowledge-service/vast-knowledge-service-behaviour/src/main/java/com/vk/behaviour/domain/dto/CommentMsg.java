package com.vk.behaviour.domain.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
public class CommentMsg extends  BaseMsgDto{
    /**
     * 用户名称
     */
    private String articleName;
    /**
     * 评论id
     */
    private Long commentId;

    /**
     * 文章ID
     */
    private Long articleId;

    /**
     * 被点赞人
     */
    private Long repayAuthorId;

    /**
     * 点赞内容类型 0:文章 1:评论
     */
    private Integer type;

    /**
     * 0:点赞 1:取消点赞
     */
    private Integer operation;

    /**
     * 登录时间
     */
    private LocalDateTime createdTime;
}
