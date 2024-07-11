package com.vk.article.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.vk.article.domain.ApArticleConfig;
import com.vk.article.mapper.ApArticleConfigMapper;
import com.vk.article.service.ApArticleConfigService;
import org.springframework.stereotype.Service;

/**
 * APP已发布文章配置 服务层实现。
 *
 * @author 张三
 * @since 2024-07-11
 */
@Service
public class ApArticleConfigServiceImpl extends ServiceImpl<ApArticleConfigMapper, ApArticleConfig> implements ApArticleConfigService {

}
