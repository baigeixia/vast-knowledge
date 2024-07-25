package com.vk.article.domain.dto;

import com.vk.article.domain.ApArticleConfig;
import lombok.Data;

import java.util.List;

@Data
public class ArticleAndConfigDto {

    private Long articleId;

    /**
     * 标题
     */
    private String title;

    /**
     * 文章所属频道ID
     */
    private Long channelId;

    /**
     * 首页图片
     */
    private String images;

    /**
     * 标签id , 多个用逗号分隔
     */
    private String labels;


    /**
     * 文章配置
     */
    private ApArticleConfig config;
}
