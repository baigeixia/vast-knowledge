package com.vk.user.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.vk.user.domain.ApUserFollow;
import com.vk.user.mapper.ApUserFollowMapper;
import com.vk.user.service.ApUserFollowService;
import org.springframework.stereotype.Service;

/**
 * APP用户关注信息 服务层实现。
 *
 * @author 张三
 * @since 2024-05-13
 */
@Service
public class ApUserFollowServiceImpl extends ServiceImpl<ApUserFollowMapper, ApUserFollow> implements ApUserFollowService {

}
