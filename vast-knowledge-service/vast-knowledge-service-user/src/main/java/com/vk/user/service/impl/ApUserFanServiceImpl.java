package com.vk.user.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.vk.user.domain.ApUserFan;
import com.vk.user.mapper.ApUserFanMapper;
import com.vk.user.service.ApUserFanService;
import org.springframework.stereotype.Service;

/**
 * APP用户粉丝信息 服务层实现。
 *
 * @author 张三
 * @since 2024-05-13
 */
@Service
public class ApUserFanServiceImpl extends ServiceImpl<ApUserFanMapper, ApUserFan> implements ApUserFanService {

}
