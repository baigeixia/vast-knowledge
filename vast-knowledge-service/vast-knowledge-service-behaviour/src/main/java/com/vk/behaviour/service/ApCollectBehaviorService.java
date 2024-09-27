package com.vk.behaviour.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import com.vk.article.domain.HomeArticleListVo;
import com.vk.behaviour.domain.ApCollectBehavior;

import java.util.List;

/**
 * APP收藏行为 服务层。
 *
 * @author 张三
 * @since 2024-09-03
 */
public interface ApCollectBehaviorService extends IService<ApCollectBehavior> {

    List<HomeArticleListVo> userCollectList(Long page, Long size, Long userId);
}
