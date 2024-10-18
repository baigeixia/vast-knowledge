package com.vk.search.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.vk.article.domain.HomeArticleListVo;
import com.vk.article.feign.RemoteClientArticleQueryService;
import com.vk.search.domain.ApUserSearch;
import com.vk.search.mapper.ApUserSearchMapper;
import com.vk.search.service.ApUserSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * APP用户搜索信息 服务层实现。
 *
 * @author 张三
 * @since 2024-05-13
 */
@Service
public class ApUserSearchServiceImpl extends ServiceImpl<ApUserSearchMapper, ApUserSearch> implements ApUserSearchService {

    @Autowired
    private RemoteClientArticleQueryService remoteClientArticleQueryService;
    /**
     * @param query  搜索内容
     * @param type   头部标题 0 综合     1 文章  3 标签 4 用户
     * @param sort   排序    0 综合排序  1 最新  2 最热
     * @param period 时间    1 不限      2 最新一天  3 最近一周  4最近一月
     * @param page 页数
     * @param size 长度
     * @return
     */
    @Override
    public List<HomeArticleListVo> searchInfo(String query, Integer type, Integer sort, Integer period, Long page, Long size) {


        return null;
    }
}
