package com.vk.article.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import com.vk.article.domain.ApArticle;
import com.vk.article.domain.HomeArticleListVo;
import com.vk.article.domain.dto.ArticleAndConfigDto;
import com.vk.article.domain.vo.ArticleInfoVo;
import com.vk.article.domain.vo.ArticleListVo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

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

    Page<HomeArticleListVo> listArticle(Long page, Long size, Integer tag,Integer type);

    Page<ArticleListVo> articleListArticle(Long page, Long size, Integer status, String title, Long channelId, String startTime, String endTime);

    Map<Long, String> getArticleTitle(Set<Long> ids);

    Map<Long, HomeArticleListVo> getArticleIdList(Set<Long> ids);

    Map<Long, HomeArticleListVo> getBehaviorArticleIdList(Long userId ,Set<Long> ids,Long page);

    Page<HomeArticleListVo> userArticleList(Long page, Long size,Integer type, Long userId);

    List<HomeArticleListVo> getSearchArticleList(String query, Integer type, Integer sort, Integer period, Long page, Long size);

    Long selectCount(LocalDateTime now);

    void importAll(long page, Long size, CountDownLatch countDownLatch, LocalDateTime now);

    void deleteOne(Long id);
}
