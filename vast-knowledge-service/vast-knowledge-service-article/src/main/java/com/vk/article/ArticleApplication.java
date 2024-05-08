package com.vk.article;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @version 1.0
 * @description 描述
 * @package com.vk
 */
@SpringBootApplication
@MapperScan(basePackages = "com.vk.article.mapper")
// @EnableFeignClients(basePackages = "com.vk.*.feign")
public class ArticleApplication {
    public static void main(String[] args) {
        SpringApplication.run(ArticleApplication.class,args);
    }

}