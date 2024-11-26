package com.vk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
@EnableScheduling
@EnableFeignClients(basePackages = "com.vk.*.feign")
public class DfsApplication {
    public static void main(String[] args) {
        SpringApplication.run(DfsApplication.class,args);
    }

}