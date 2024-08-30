package com.vk.behaviour.domain.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class LikesBehaviorDto implements Serializable {

    private Long id;

    /**
     * 文章ID
     */
    private Long articleId;

    /**
     * 评论id
     */
    private Long commentId;

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


}
