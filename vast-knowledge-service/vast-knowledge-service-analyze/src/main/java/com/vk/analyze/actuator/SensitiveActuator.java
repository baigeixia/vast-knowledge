package com.vk.analyze.actuator;


import com.vk.analyze.mapper.AdSensitiveMapper;
import com.vk.common.redis.utlis.SensitiveWord;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class SensitiveActuator {

    private static final Logger log = LoggerFactory.getLogger(SensitiveActuator.class);

    private final SensitiveWord sensitiveWord;
    private final AdSensitiveMapper adSensitiveMapper;

    @Autowired
    public SensitiveActuator(SensitiveWord sensitiveWord, AdSensitiveMapper adSensitiveMapper) {
        this.sensitiveWord = sensitiveWord;
        this.adSensitiveMapper = adSensitiveMapper;
    }

    @PostConstruct
    public void init() {
        try {
            sensitiveWord.loadSensitiveWordsAsync();
        } catch (Exception e) {
            log.error("敏感词初始化错误");
        }
        // 确保该方法只执行一次
        Set<String> matchSensitive = adSensitiveMapper.sensitiveSet(0);
        sensitiveWord.addSensitive(matchSensitive);
        Set<String> excludeSensitives = adSensitiveMapper.sensitiveSet(1);
        sensitiveWord.removeSensitive(excludeSensitives);
    }

}
