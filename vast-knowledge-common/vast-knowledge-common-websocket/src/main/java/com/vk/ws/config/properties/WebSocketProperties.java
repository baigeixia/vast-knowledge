package com.vk.ws.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * WebSocket 配置项
 *
 * @author zendwang
 */
@ConfigurationProperties("websocket")
@Data
@RefreshScope
public class WebSocketProperties {

    private Boolean enabled;

    /**
     * 路径
     */
    private String path;

    /**
     *  设置访问源地址
     */
    private String allowedOrigins;
}
