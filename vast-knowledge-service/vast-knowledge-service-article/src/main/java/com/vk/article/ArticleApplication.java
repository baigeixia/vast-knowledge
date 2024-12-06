package com.vk.article;

import com.vk.db.repository.article.ArticleMgRepository;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @version 1.0
 * @description 描述
 * @package com.vk
 */
@SpringBootApplication
@MapperScan(basePackages = "com.vk.article.mapper")
@EnableMongoRepositories(basePackageClasses = ArticleMgRepository.class)
@EnableFeignClients(basePackages = "com.vk.*.feign")
@EnableElasticsearchRepositories(basePackages = "com.vk.common.es.repository")
public class ArticleApplication {
    public static void main(String[] args) {
        SpringApplication.run(ArticleApplication.class,args);
    }
}