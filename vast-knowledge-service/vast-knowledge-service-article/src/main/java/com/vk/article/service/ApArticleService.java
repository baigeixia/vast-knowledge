package com.vk.article.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import com.vk.article.domain.ApArticle;
import com.vk.article.domain.dto.ArticleAndConfigDto;
import com.vk.article.domain.vo.ArticleInfoVo;
import com.vk.article.domain.dto.HomeArticleListDto;
import com.vk.article.domain.vo.ArticleListVo;

import java.time.LocalDateTime;

/**
 * 已发布的文章信息 服务层。
 *
 * @author 张三
 * @since 2024-07-11
 */
public interface ApArticleService extends IService<ApArticle> {

    Long saveArticle(ApArticle apArticle);

    ArticleInfoVo infoArticle(Long articleId);

    Long saveOrUpArticle(ArticleAndConfigDto dto);

    Page<HomeArticleListDto> listArticle(Long page, Long size, Integer tag);

    Page<ArticleListVo> articleListArticle(Long page, Long size, Integer status, String title, Long channelId, LocalDateTime startTime, LocalDateTime endTime);
}
