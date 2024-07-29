package com.vk.article.domain.dto;

import lombok.Data;

@Data
public class HomeArticleListVo {

    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 简单描述
     */
    private String simpleDescription;

    /**
     * 文章作者的ID
     */
    private Long authorId;

    /**
     * 作者昵称
     */
    private String authorName;

    /**
     * 文章所属频道ID
     */
    private Long channelId;

    /**
     * 频道名称
     */
    private String channelName;


    /**
     * 文章图片,多张逗号分隔
     */
    private String images;

    /**
     * 文章标签最多3个,逗号分隔
     */
    private String labels;


    /**
     * 点赞数量
     */
    private Long likes;

    /**
     * 收藏数量
     */
    private Long collection;

    /**
     * 评论数量
     */
    private Long comment;

    /**
     * 阅读数量
     */
    private Long views;

    /**
     * 是否可评论
     */
    private Integer isComment;

    /**
     * 是否转发
     */
    private Integer isForward;

    /**
     * 是否下架
     */
    private Integer isDown;

    /**
     * 是否已删除
     */
    private Integer isDelete;
}
