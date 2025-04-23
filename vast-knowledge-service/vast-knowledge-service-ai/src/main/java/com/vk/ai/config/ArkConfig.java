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

    private String apiKey;

    private String baseUrl;

    private Integer timeout;

    private Integer connectTimeout;

    private Integer retryTimes;

}
