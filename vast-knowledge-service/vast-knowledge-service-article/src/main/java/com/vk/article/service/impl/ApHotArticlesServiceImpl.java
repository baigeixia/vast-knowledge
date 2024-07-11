package com.vk.article.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.vk.article.domain.ApHotArticles;
import com.vk.article.mapper.ApHotArticlesMapper;
import com.vk.article.service.ApHotArticlesService;
import org.springframework.stereotype.Service;

/**
 * 热点文章 服务层实现。
 *
 * @author 张三
 * @since 2024-07-11
 */
@Service
public class ApHotArticlesServiceImpl extends ServiceImpl<ApHotArticlesMapper, ApHotArticles> implements ApHotArticlesService {

}
