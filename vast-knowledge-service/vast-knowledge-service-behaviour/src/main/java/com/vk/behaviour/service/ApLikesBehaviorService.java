package com.vk.behaviour.service;

import com.mybatisflex.core.service.IService;
import com.vk.behaviour.domain.ApLikesBehavior;
import com.vk.behaviour.domain.vo.ArticleAndCommentLikeVo;
import com.vk.behaviour.domain.vo.notification.like.LikeNotificationListVo;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * APP点赞行为 服务层。
 *
 * @author 张三
 * @since 2024-05-13
 */
public interface ApLikesBehaviorService extends IService<ApLikesBehavior> {



    List<LikeNotificationListVo> likeList(Long page, Long size);


    Map<Long, Integer> articleLike(Set<Long> ids);

    ArticleAndCommentLikeVo commentLike(Long artId, Set<Long> ids);
}
