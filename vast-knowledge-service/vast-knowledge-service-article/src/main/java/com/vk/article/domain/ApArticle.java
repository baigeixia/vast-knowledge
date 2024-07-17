package com.vk.article.domain;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import java.time.LocalDateTime;

/**
 * 已发布的文章信息 实体类。
 *
 * @author 张三
 * @since 2024-07-11
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "ap_article")
public class ApArticle implements Serializable {

    @Id
    private Long id;

    /**
     * 标题
     */
    private String title;

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
     * 文章布局 0:无图文章 1:单图文章 2:多图文章
     */
    private Integer layout;

    /**
     * 文章标记 0:普通文章 1:热点文章 2:置顶文章 3:精品文章 4:大V文章
     */
    private Integer flag;

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
     * 省市
     */
    private Long provinceId;

    /**
     * 市区
     */
    private Long cityId;

    /**
     * 区县
     */
    private Long countyId;

    /**
     * 创建时间
     */
    private LocalDateTime createdTime;

    /**
     * 发布时间
     */
    private LocalDateTime publishTime;

    /**
     * 同步状态
     */
    private Boolean syncStatus;

    /**
     * 来源
     */
    private Integer origin;

}
