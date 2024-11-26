package com.vk.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@Configuration
@RefreshScope
@ConfigurationProperties(prefix = "dfs")
public class DfsConfig {
    private String type;
    private List<String> matchExt;
}
