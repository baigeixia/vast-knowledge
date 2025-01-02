package com.vk.analyze.actuator;


import com.mybatisflex.core.query.QueryWrapper;
import com.vk.analyze.domain.AdSensitive;
import com.vk.analyze.domain.table.AdSensitiveTableDef;
import com.vk.analyze.mapper.AdSensitiveMapper;
import com.vk.common.core.utils.SensitiveWord;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

import static com.vk.analyze.domain.table.AdSensitiveTableDef.AD_SENSITIVE;

@Component
public class SensitiveActuator {

    @Autowired
    private SensitiveWord sensitiveWord;

    @Autowired
    private AdSensitiveMapper adSensitiveMapper;

    @PostConstruct
    public void init() {
        // 确保该方法只执行一次
        Set<String> matchSensitive = adSensitiveMapper.sensitiveSet(0);
        sensitiveWord.addSensitive(matchSensitive);
        Set<String> excludeSensitives = adSensitiveMapper.sensitiveSet(1);
        sensitiveWord.removeSensitive(excludeSensitives);
    }


//    @Override
//    public void onApplicationEvent(ContextRefreshedEvent event) {
//        Set<String> matchSensitive = adSensitiveMapper.sensitiveSet(0);
//        sensitiveWord.addSensitive(matchSensitive);
//        Set<String> excludeSensitives = adSensitiveMapper.sensitiveSet(1);
//        sensitiveWord.removeSensitive(excludeSensitives);
//    }
}
