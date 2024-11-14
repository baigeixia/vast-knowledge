package com.vk.search.service;

import com.mybatisflex.core.service.IService;
import com.vk.search.domain.ApHotWords;

import java.util.List;

/**
 * 搜索热词 服务层。
 *
 * @author 张三
 * @since 2024-05-13
 */
public interface ApHotWordsService extends IService<ApHotWords> {

    List<ApHotWords> getTrending();
}
