package com.vk.analyze.service;

import com.mybatisflex.core.service.IService;
import com.vk.analyze.domain.AdChannel;

import java.util.List;

/**
 * 频道信息 服务层。
 *
 * @author 张三
 * @since 2024-05-13
 */
public interface AdChannelService extends IService<AdChannel> {

    List<AdChannel> getlist(AdChannel adChannel);
}
