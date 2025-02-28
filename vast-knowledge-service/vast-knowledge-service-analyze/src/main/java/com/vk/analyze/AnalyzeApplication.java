package com.vk.analyze;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@SpringBootApplication
@MapperScan(basePackages = "com.vk.analyze.mapper")
@EnableFeignClients(basePackages = "com.vk.*.feign")
public class AnalyzeApplication {
    public static void main(String[] args) {
        SpringApplication.run(AnalyzeApplication.class, args);
    }


}