package com.vk.behaviour.service;

import com.mybatisflex.core.service.IService;
import com.vk.behaviour.domain.ApLikesBehavior;
import com.vk.behaviour.domain.dto.LikesBehaviorDto;

/**
 * APP点赞行为 服务层。
 *
 * @author 张三
 * @since 2024-05-13
 */
public interface ApLikesBehaviorService extends IService<ApLikesBehavior> {

    void saveLike(LikesBehaviorDto dto);
}
