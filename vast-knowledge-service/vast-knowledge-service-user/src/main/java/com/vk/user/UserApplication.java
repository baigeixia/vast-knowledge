package com.vk.user;

import com.vk.common.security.annotation.EnableCustomConfig;
import com.vk.common.security.annotation.EnableRyFeignClients;
import com.vk.common.swagger.annotation.EnableCustomSwagger2;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableCustomConfig
@EnableCustomSwagger2
@EnableRyFeignClients
@SpringBootApplication
@MapperScan(basePackages = "com.vk.user.mapper")
@EnableFeignClients(basePackages = "com.vk.*.feign")
public class UserApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class,args);
    }

}