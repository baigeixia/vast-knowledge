package com.vk.article.domain.vo;

import com.vk.article.domain.ApArticle;
import com.vk.article.domain.ApArticleConfig;
import lombok.Data;

/**
 * 已发布的文章信息 实体类。
 *
 * @author 张三
 * @since 2024-07-11
 */

@Data
public class ArticleInfoVo extends ApArticle {

   private ApArticleConfig config;

}
