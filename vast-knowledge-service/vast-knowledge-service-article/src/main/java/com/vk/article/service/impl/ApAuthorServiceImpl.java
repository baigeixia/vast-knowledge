package com.vk.article.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.vk.article.domain.ApAuthor;
import com.vk.article.mapper.ApAuthorMapper;
import com.vk.article.service.ApAuthorService;
import org.springframework.stereotype.Service;

/**
 * APP文章作者信息 服务层实现。
 *
 * @author 张三
 * @since 2024-07-11
 */
@Service
public class ApAuthorServiceImpl extends ServiceImpl<ApAuthorMapper, ApAuthor> implements ApAuthorService {

}
