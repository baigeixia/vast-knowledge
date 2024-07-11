package com.vk.article.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.vk.article.domain.ApArticle;
import com.vk.article.mapper.ApArticleMapper;
import com.vk.article.service.ApArticleService;
import org.springframework.stereotype.Service;

/**
 * 已发布的文章信息 服务层实现。
 *
 * @author 张三
 * @since 2024-07-11
 */
@Service
public class ApArticleServiceImpl extends ServiceImpl<ApArticleMapper, ApArticle> implements ApArticleService {

}
