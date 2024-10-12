package com.vk.behaviour.domain.vo;

import lombok.Data;

import java.util.Map;

@Data
public class ArticleAndCommentLikeVo {
    /**
     * 文章点赞 0点赞 1取消点赞
     */
    private Integer articleLike;
    /**
     * 文章收藏
     */
    private Boolean articleCollect;
    /**
     * 评论点赞
     */
    private Map<Long, Integer> commentLike;
}
