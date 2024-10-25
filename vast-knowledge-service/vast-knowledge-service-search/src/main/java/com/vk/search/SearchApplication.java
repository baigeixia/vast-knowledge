package com.vk.search;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.elasticsearch.repository.config.EnableReactiveElasticsearchRepositories;

@SpringBootApplication
@MapperScan(basePackages = "com.vk.search.mapper")
@EnableFeignClients(basePackages = "com.vk.*.feign")
@EnableElasticsearchRepositories(basePackages = "com.vk.common.es.repository")
public class SearchApplication {
    public static void main(String[] args) {
        SpringApplication.run(SearchApplication.class,args);
    }

}