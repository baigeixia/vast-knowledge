package com.vk.article.service;

import com.mybatisflex.core.service.IService;
import com.vk.article.domain.ApArticleConfig;

/**
 * APP已发布文章配置 服务层。
 *
 * @author 张三
 * @since 2024-07-11
 */
public interface ApArticleConfigService extends IService<ApArticleConfig> {

    void deleteArticle(Long id);
}
