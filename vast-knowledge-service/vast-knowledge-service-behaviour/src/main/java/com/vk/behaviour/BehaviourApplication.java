package com.vk.behaviour;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@MapperScan(basePackages = "com.vk.behaviour.mapper")
@EnableFeignClients(basePackages = "com.vk.*.feign")
public class BehaviourApplication {
    public static void main(String[] args) {
        SpringApplication.run(BehaviourApplication.class,args);
    }

}