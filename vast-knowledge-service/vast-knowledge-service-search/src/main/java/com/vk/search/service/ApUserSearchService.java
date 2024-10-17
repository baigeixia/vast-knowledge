package com.vk.search.service;

import com.mybatisflex.core.service.IService;
import com.vk.article.domain.HomeArticleListVo;
import com.vk.search.domain.ApUserSearch;

import java.util.List;

/**
 * APP用户搜索信息 服务层。
 *
 * @author 张三
 * @since 2024-05-13
 */
public interface ApUserSearchService extends IService<ApUserSearch> {

    List<HomeArticleListVo> searchInfo(String query, Integer type, Integer sort, Integer period,Long page ,Long size);
}
