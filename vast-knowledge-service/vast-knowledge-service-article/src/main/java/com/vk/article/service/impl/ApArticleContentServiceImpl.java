package com.vk.article.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.vk.article.domain.ApArticleContent;
import com.vk.article.mapper.ApArticleContentMapper;
import com.vk.article.service.ApArticleContentService;
import org.springframework.stereotype.Service;

/**
 * APP已发布文章内容 服务层实现。
 *
 * @author 张三
 * @since 2024-07-11
 */
@Service
public class ApArticleContentServiceImpl extends ServiceImpl<ApArticleContentMapper, ApArticleContent> implements ApArticleContentService {

}
