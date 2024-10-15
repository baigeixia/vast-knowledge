package com.vk.behaviour.service;

import com.mybatisflex.core.service.IService;
import com.vk.behaviour.domain.ApReadBehavior;
import com.vk.behaviour.domain.vo.UserFootMarkListVo;

import java.util.List;

/**
 * APP阅读行为 服务层。
 *
 * @author 张三
 * @since 2024-05-13
 */
public interface ApReadBehaviorService extends IService<ApReadBehavior> {

    List<UserFootMarkListVo> getUserFootMark(Long page, Long size);

    ApReadBehavior getArticleInfo(Long id);
}
