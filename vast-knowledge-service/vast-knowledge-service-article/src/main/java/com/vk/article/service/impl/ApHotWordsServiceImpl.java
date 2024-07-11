package com.vk.article.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.vk.article.domain.ApHotWords;
import com.vk.article.mapper.ApHotWordsMapper;
import com.vk.article.service.ApHotWordsService;
import org.springframework.stereotype.Service;

/**
 * 搜索热词 服务层实现。
 *
 * @author 张三
 * @since 2024-07-11
 */
@Service
public class ApHotWordsServiceImpl extends ServiceImpl<ApHotWordsMapper, ApHotWords> implements ApHotWordsService {

}
