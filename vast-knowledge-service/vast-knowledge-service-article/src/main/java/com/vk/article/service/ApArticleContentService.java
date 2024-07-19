package com.vk.article.service;

import com.mybatisflex.core.service.IService;
import com.vk.article.domain.ApArticleContent;

/**
 * APP已发布文章内容 服务层。
 *
 * @author 张三
 * @since 2024-07-11
 */
public interface ApArticleContentService extends IService<ApArticleContent> {

    Long contentSave(ApArticleContent apArticleContent);

    ApArticleContent getInfoContent(Long id);
}
