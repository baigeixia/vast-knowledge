package com.vk.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 网关启动程序
 * 
 * @author vk
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class LoginGetawayApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(LoginGetawayApplication.class, args);
    }
}
