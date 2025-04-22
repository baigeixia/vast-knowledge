package com.vk.ai.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@Data
@ConfigurationProperties(prefix = "aihub.ark")
public class ArkConfig {

    @Value("${apikey}")
    private String apiKey;

    @Value("${baseUrl}")
    private String baseUrl;

    @Value("${timeout}")
    private Integer timeout;

    @Value("${connectTimeout}")
    private Integer connectTimeout;

    @Value("${retryTimes}")
    private Integer retryTimes;

}
