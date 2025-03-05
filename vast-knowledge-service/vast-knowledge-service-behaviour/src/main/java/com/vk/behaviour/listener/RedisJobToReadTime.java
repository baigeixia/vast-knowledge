package com.vk.behaviour.listener;


import com.vk.common.redis.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Slf4j
public class RedisJobToReadTime {

    @Autowired
    private RedisService redisService;
    @Scheduled(fixedRate = 3000) // 每分钟执行一次
    public void insertExpiredDataToMySQL() {
        Set<String> keys = redisService.getCacheSet("read:*");
        for (String key : keys) {
            log.info(key);
        }
    }
}
