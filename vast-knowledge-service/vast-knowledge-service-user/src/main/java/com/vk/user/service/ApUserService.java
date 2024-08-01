package com.vk.user.service;

import com.mybatisflex.core.service.IService;
import com.vk.user.domain.ApUser;
import com.vk.user.domain.AuthorInfo;

import java.util.Map;
import java.util.Set;

/**
 * APP用户信息 服务层。
 *
 * @author 张三
 * @since 2024-05-13
 */
public interface ApUserService extends IService<ApUser> {

    Map<Long, AuthorInfo> getUserList(Set<Long> userId);
}
