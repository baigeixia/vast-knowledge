package com.vk.ai;

import com.vk.db.repository.aiMessage.AiMgRepository;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@MapperScan(basePackages = "com.vk.ai.mapper")
@EnableMongoRepositories(basePackageClasses = AiMgRepository.class)
public class AiApplication {
    public static void main(String[] args) {
        SpringApplication.run(AiApplication.class,args);
    }


}