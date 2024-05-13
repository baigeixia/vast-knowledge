package com.vk.admin.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.vk.admin.domain.ApUserInfo;
import com.vk.admin.mapper.ApUserInfoMapper;
import com.vk.admin.service.ApUserInfoService;
import org.springframework.stereotype.Service;

/**
 * APP用户详情信息 服务层实现。
 *
 * @author 张三
 * @since 2024-05-13
 */
@Service
public class ApUserInfoServiceImpl extends ServiceImpl<ApUserInfoMapper, ApUserInfo> implements ApUserInfoService {

}
