package com.vk.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RefreshScope
@ConfigurationProperties(prefix = "tx")
public class TxDfsConfig {
    private String secretId ;
    private String secretKey ;
}
