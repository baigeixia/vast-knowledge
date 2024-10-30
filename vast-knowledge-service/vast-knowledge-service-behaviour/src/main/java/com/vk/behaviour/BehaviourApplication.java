package com.vk.behaviour;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@SpringBootApplication
@MapperScan(basePackages = "com.vk.behaviour.mapper")
@EnableFeignClients(basePackages = "com.vk.*.feign")
@EnableElasticsearchRepositories(basePackages = "com.vk.common.es.repository")
public class BehaviourApplication {
    public static void main(String[] args) {
        SpringApplication.run(BehaviourApplication.class,args);
    }

}