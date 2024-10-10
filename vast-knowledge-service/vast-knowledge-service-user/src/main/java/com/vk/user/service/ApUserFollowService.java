package com.vk.user.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import com.vk.user.domain.ApUserFollow;
import com.vk.user.domain.vo.FollowListVo;

/**
 * APP用户关注信息 服务层。
 *
 * @author 张三
 * @since 2024-05-13
 */
public interface ApUserFollowService extends IService<ApUserFollow> {

    Page<FollowListVo> getList(Long page, Long size, Long userId);
}
