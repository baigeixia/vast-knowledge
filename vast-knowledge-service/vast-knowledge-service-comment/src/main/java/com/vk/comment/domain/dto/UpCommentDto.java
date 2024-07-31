package com.vk.comment.domain.dto;

import lombok.Data;

@Data
public class UpCommentDto {

    private Long commentId;

    private Long commentRepayId;

    private Long entryId;

    /**
     * 文章标记 0:普通评论 1:热点评论 2:推荐评论 3:置顶评论 4:精品评论 5:大V评论
     */
    private Integer flag;

    private  Integer status;
}
