package com.vk.analyze.service;

import com.mybatisflex.core.service.IService;
import com.vk.analyze.domain.AdSensitive;

import java.util.List;

/**
 * 敏感词信息 服务层。
 *
 * @author 张三
 * @since 2024-05-13
 */
public interface AdSensitiveService extends IService<AdSensitive> {

    List<AdSensitive> getlist(AdSensitive adSensitive);
}
