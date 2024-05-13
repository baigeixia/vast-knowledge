package com.vk.analyze.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.vk.analyze.domain.AdArticleStatistics;
import com.vk.analyze.mapper.AdArticleStatisticsMapper;
import com.vk.analyze.service.AdArticleStatisticsService;
import org.springframework.stereotype.Service;

/**
 * 文章数据统计 服务层实现。
 *
 * @author 张三
 * @since 2024-05-13
 */
@Service
public class AdArticleStatisticsServiceImpl extends ServiceImpl<AdArticleStatisticsMapper, AdArticleStatistics> implements AdArticleStatisticsService {

}
