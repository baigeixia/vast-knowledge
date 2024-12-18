package com.vk.common.filter;

import com.vk.common.core.utils.ServletUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class DoubleTokenGatewayFilter implements GatewayFilter, Ordered {
    private static final Logger log = LoggerFactory.getLogger(DoubleTokenGatewayFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 获取请求中的两个 Token
        String accessToken = exchange.getRequest().getHeaders().getFirst("Authorization");
        String refreshToken = exchange.getRequest().getHeaders().getFirst("X-Refresh-Token");
        log.info("DoubleTokenGatewayFilter----------->>>>");
        // if (accessToken == null && refreshToken != null) {
        //     return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid Access Token"));
        // }
        // // 校验 Access Token
        // if (accessToken == null || !isValidAccessToken(accessToken)) {
        //     return Mono.error(new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid Access Token"));
        // }
        //
        // // 校验 Refresh Token
        // if (refreshToken != null && !isValidRefreshToken(refreshToken)) {
        //     return Mono.error(new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid Refresh Token"));
        // }
        //
        // // 如果有 Refresh Token 可以尝试刷新 Access Token（具体逻辑可根据业务需求）
        // if (refreshToken != null) {
        //     // 刷新逻辑
        //     String newAccessToken = refreshAccessToken(refreshToken);
        //     // 设置新的 Access Token 到请求头中
        //     exchange.getRequest().mutate().header("Authorization", "Bearer " + newAccessToken).build();
        // }

        return chain.filter(exchange);
    }
    private Mono<Void> unauthorizedResponse(ServerWebExchange exchange, String msg)
    {
        log.error("[登录异常处理]请求路径:{}", exchange.getRequest().getPath());
        return ServletUtils.webFluxResponseWriter(exchange.getResponse(), msg, com.vk.common.core.constant.HttpStatus.UNAUTHORIZED);
    }


    private boolean isValidAccessToken(String token) {
        // 校验 Access Token 是否有效（可以通过 JWT 或 OAuth 服务）
        return true; // 示例
    }

    private boolean isValidRefreshToken(String token) {
        // 校验 Refresh Token 是否有效
        return true; // 示例
    }

    private String refreshAccessToken(String refreshToken) {
        // 刷新 Access Token 的逻辑
        return "newAccessToken"; // 示例
    }

    @Override
    public int getOrder() {
        return -2; // 确保过滤器在认证之前执行
    }
}
