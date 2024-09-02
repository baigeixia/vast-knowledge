package com.vk.user;


import com.vk.common.redis.service.RedisService;
import com.vk.common.security.utils.SecurityUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.vk.common.redis.constants.BusinessConstants.SOCKET_USER_ID;

@SpringBootTest
class TestMAches {
    @Autowired
    private RedisService redisService;

    @Test
    void contextLoads() {
        // String rawPassword="123456";
        // String salt="123abc";
        // System.out.println(SecurityUtils.encryptPassword(rawPassword, salt));

        String object = redisService.getCacheObject(SOCKET_USER_ID + 1);
        System.out.println(object);

    }

}
