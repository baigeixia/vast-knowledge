package com.vk.search.jobhandler;


import com.vk.common.redis.service.RedisService;
import com.vk.search.domain.HotWordsTop;
import com.vk.search.mapper.ApHotWordsMapper;
import com.vk.search.service.ApHotWordsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

@Component
@Slf4j(topic = "HotWordsJob")
public class HotWordsJob {

    @Autowired
    private ApHotWordsMapper apHotWordsMapper;

    @Autowired
    private RedisService redisService;

    private static final ReentrantLock lock = new ReentrantLock();


    // @XxlJob("hotWordsSyncRedis")
    public void hotWordsSyncRedis() {
        if (lock.tryLock()) {
            try {
                List<HotWordsTop> tops = apHotWordsMapper.selectTopHotList();
                redisService.setCacheObject("hot_tops", tops);
            } catch (Exception e) {
                log.error("定时储存热点关键词错误,{}", e.getMessage());
            } finally {
                lock.unlock();
            }
        }
    }
}
