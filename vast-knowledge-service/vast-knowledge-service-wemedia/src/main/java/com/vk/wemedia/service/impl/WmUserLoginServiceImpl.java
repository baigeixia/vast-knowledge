package com.vk.wemedia.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.vk.wemedia.domain.WmUserLogin;
import com.vk.wemedia.mapper.WmUserLoginMapper;
import com.vk.wemedia.service.WmUserLoginService;
import org.springframework.stereotype.Service;

/**
 * 自媒体用户登录行为信息 服务层实现。
 *
 * @author 张三
 * @since 2024-05-13
 */
@Service
public class WmUserLoginServiceImpl extends ServiceImpl<WmUserLoginMapper, WmUserLogin> implements WmUserLoginService {

}
