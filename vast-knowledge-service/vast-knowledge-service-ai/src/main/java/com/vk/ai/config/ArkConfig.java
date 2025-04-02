package com.vk.ai.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class ArkConfig {

    @Value("${ark.apikey}")
    private String apiKey;

    @Value("${ark.baseUrl}")
    private String baseUrl;

    @Value("${ark.timeout}")
    private Integer timeout;

    @Value("${ark.connectTimeout}")
    private Integer connectTimeout;

    @Value("${ark.retryTimes}")
    private Integer retryTimes;

}
