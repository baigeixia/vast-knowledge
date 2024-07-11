package com.vk.article.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.vk.article.domain.ApCollection;
import com.vk.article.mapper.ApCollectionMapper;
import com.vk.article.service.ApCollectionService;
import org.springframework.stereotype.Service;

/**
 * APP收藏信息 服务层实现。
 *
 * @author 张三
 * @since 2024-07-11
 */
@Service
public class ApCollectionServiceImpl extends ServiceImpl<ApCollectionMapper, ApCollection> implements ApCollectionService {

}
