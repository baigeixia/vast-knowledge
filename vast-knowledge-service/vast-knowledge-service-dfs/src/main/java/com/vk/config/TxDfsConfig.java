package com.vk.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

@Configuration
@RefreshScope
@ConfigurationProperties(prefix = "tx")
@Data
public class TxDfsConfig {
    private String secretId;
    private String secretKey;
    private String region;
    private Integer durationSeconds;
    private String bucket;
}
