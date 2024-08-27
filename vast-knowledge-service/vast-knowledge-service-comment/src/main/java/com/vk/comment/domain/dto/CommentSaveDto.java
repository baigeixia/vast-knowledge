package com.vk.comment.domain.dto;

import lombok.Data;

@Data
public class CommentSaveDto {


    /**
     * 评论内容类型 0:文章 1:动态
     */
    private Integer type;
    /**
     * 文章作者id
     */
    private Long arAuthorId;

    /**
     * 频道ID
     */
    private Long channelId;

    private Long entryId;
    /**
     * 评论内容
     */
    private String content;

    private String image;
}
