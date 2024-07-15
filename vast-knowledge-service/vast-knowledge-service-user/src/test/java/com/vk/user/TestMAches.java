package com.vk.user;


import com.vk.common.security.utils.SecurityUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

// import org.springframework.boot.test.context.SpringBootTest;
//
//
@SpringBootTest
class TestMAches {

    @Test
    void contextLoads() {
        String rawPassword="123456";
        String salt="123abc";
        System.out.println(SecurityUtils.encryptPassword(rawPassword, salt));
    }

}
