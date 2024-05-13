package com.vk.analyze.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.vk.analyze.domain.AdSensitive;
import com.vk.analyze.mapper.AdSensitiveMapper;
import com.vk.analyze.service.AdSensitiveService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 敏感词信息 服务层实现。
 *
 * @author 张三
 * @since 2024-05-13
 */
@Service
public class AdSensitiveServiceImpl extends ServiceImpl<AdSensitiveMapper, AdSensitive> implements AdSensitiveService {

    @Override
    public List<AdSensitive> getlist(AdSensitive adSensitive) {
        return this.mapper.getlist(adSensitive);
    }
}
