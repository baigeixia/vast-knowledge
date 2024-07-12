package com.vk.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
@ComponentScan(basePackages = {"com.vk"})
public class CoreGetawayApplication {
    public static void main(String[] args)
    {
        SpringApplication.run(CoreGetawayApplication.class, args);
    }
}