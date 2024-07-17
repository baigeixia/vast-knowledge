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
 * APP用户动态信息 实体类。
 *
 * @author 张三
 * @since 2024-07-11
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "ap_dynamic")
public class ApDynamic implements Serializable {

    @Id
    private Long id;

    /**
     * 文章作者的ID
     */
    private Long userId;

    /**
     * 作者昵称
     */
    private String userName;

    /**
     * 频道名称
     */
    private String content;

    /**
     * 是否转发
     */
    private Integer isForward;

    /**
     * 转发文章ID
     */
    private Long articleId;

    /**
     * 转发文章标题
     */
    private String articelTitle;

    /**
     * 转发文章图片
     */
    private String articleImage;

    /**
     * 布局标识 0:无图动态 1:单图动态 2:多图动态 3:转发动态
     */
    private Integer layout;

    /**
     * 文章图片,多张逗号分隔
     */
    private String images;

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
     * 创建时间
     */
    private LocalDateTime createdTime;

}
