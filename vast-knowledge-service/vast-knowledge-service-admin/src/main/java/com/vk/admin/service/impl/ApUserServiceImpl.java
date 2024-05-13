package com.vk.admin.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.vk.admin.domain.ApUser;
import com.vk.admin.mapper.ApUserMapper;
import com.vk.admin.service.ApUserService;
import org.springframework.stereotype.Service;

/**
 * APP用户信息 服务层实现。
 *
 * @author 张三
 * @since 2024-05-13
 */
@Service
public class ApUserServiceImpl extends ServiceImpl<ApUserMapper, ApUser> implements ApUserService {

}
