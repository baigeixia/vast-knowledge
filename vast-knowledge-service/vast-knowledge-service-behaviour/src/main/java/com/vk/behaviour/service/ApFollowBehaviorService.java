package com.vk.behaviour.service;

import com.mybatisflex.core.service.IService;
import com.vk.behaviour.domain.ApFollowBehavior;
import com.vk.behaviour.domain.vo.notification.follow.FollowNotificationListVo;

import java.util.List;

/**
 * APP关注行为 服务层。
 *
 * @author 张三
 * @since 2024-05-13
 */
public interface ApFollowBehaviorService extends IService<ApFollowBehavior> {

    List<FollowNotificationListVo> followList(Long id,Long page, Long size);
}
