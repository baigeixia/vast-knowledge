package com.vk.wemedia.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.vk.wemedia.domain.WmUser;
import com.vk.wemedia.mapper.WmUserMapper;
import com.vk.wemedia.service.WmUserService;
import org.springframework.stereotype.Service;

/**
 * 自媒体用户信息 服务层实现。
 *
 * @author 张三
 * @since 2024-05-13
 */
@Service
public class WmUserServiceImpl extends ServiceImpl<WmUserMapper, WmUser> implements WmUserService {

}
