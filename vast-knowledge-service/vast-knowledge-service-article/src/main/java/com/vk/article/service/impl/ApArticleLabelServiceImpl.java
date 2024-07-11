package com.vk.article.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.vk.article.domain.ApArticleLabel;
import com.vk.article.mapper.ApArticleLabelMapper;
import com.vk.article.service.ApArticleLabelService;
import org.springframework.stereotype.Service;

/**
 * 文章标签信息 服务层实现。
 *
 * @author 张三
 * @since 2024-07-11
 */
@Service
public class ApArticleLabelServiceImpl extends ServiceImpl<ApArticleLabelMapper, ApArticleLabel> implements ApArticleLabelService {

}
