package com.vk.user.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.vk.user.domain.ApUserMessage;
import com.vk.user.mapper.ApUserMessageMapper;
import com.vk.user.service.ApUserMessageService;
import org.springframework.stereotype.Service;

/**
 * APP用户消息通知信息 服务层实现。
 *
 * @author 张三
 * @since 2024-05-13
 */
@Service
public class ApUserMessageServiceImpl extends ServiceImpl<ApUserMessageMapper, ApUserMessage> implements ApUserMessageService {

}
