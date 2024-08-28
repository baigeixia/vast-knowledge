package com.vk.comment.domain.dto;

import lombok.Data;

@Data
public class CommentReSaveDto {

    private Long commentId;
    /**
     * 回复评论用户id
     */
    private  Long repayAuthorId;

    private Long commentRepayId;
    /**
     * 评论内容
     */
    private String content;

    private String image;
}
