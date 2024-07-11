package com.vk.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;


@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class CoreGetawayApplication {
    public static void main(String[] args)
    {
        SpringApplication.run(CoreGetawayApplication.class, args);
    }
}