package com.vk.system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * 网关启动程序
 * 
 * @author vk
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
@ComponentScan(basePackages = {"com.vk"})
public class SystemGetawayApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(SystemGetawayApplication.class, args);
    }
}
