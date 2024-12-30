package com.vk.analyze;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@MapperScan(basePackages = "com.vk.analyze.mapper")
@EnableFeignClients(basePackages = "com.vk.*.feign")
public class AnalyzeApplication {
    public static void main(String[] args) {
        SpringApplication.run(AnalyzeApplication.class, args);
    }
}