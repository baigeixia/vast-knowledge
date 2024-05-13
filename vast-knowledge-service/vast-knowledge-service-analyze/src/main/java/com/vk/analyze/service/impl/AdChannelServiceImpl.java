package com.vk.analyze.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.vk.analyze.domain.AdChannel;
import com.vk.analyze.mapper.AdChannelMapper;
import com.vk.analyze.service.AdChannelService;
import org.springframework.stereotype.Service;

/**
 * 频道信息 服务层实现。
 *
 * @author 张三
 * @since 2024-05-13
 */
@Service
public class AdChannelServiceImpl extends ServiceImpl<AdChannelMapper, AdChannel> implements AdChannelService {

}
