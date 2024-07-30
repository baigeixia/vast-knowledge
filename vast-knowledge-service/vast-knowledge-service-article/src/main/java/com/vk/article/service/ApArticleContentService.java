package com.vk.article.service;

import com.mybatisflex.core.service.IService;
import com.vk.article.domain.ApArticleContent;
import com.vk.article.domain.dto.SaveArticleContentDto;

/**
 * APP已发布文章内容 服务层。
 *
 * @author 张三
 * @since 2024-07-11
 */
public interface ApArticleContentService extends IService<ApArticleContent> {

    Long contentSave(SaveArticleContentDto dto);

    ApArticleContent getInfoContent(Long id);
}
