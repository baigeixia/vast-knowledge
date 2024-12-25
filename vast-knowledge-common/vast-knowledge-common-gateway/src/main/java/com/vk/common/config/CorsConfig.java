package com.vk.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.cors.reactive.CorsUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * 跨域配置
 * 
 * @author ruoyi
 */
@Configuration
public class CorsConfig
{
    // private static final String ALLOWED_HEADERS = "*";
    private static final String ALLOWED_METHODS = "GET,POST,PUT,DELETE,OPTIONS,HEAD";
    private static final String ALLOWED_ORIGIN = "*";
    private static final String ALLOWED_EXPOSE = "*";
    private static final String MAX_AGE = "18000";
    private static final String ALLOWED_HEADERS = "Content-Type,user-authorization,admin-authorization,from,X-Refresh-Token,istoken,repeatsubmit";

    // @Bean
    // public WebFilter corsFilter()
    // {
    //     return (ServerWebExchange ctx, WebFilterChain chain) -> {
    //         ServerHttpRequest request = ctx.getRequest();
    //         if (CorsUtils.isCorsRequest(request))
    //         {
    //             ServerHttpResponse response = ctx.getResponse();
    //             HttpHeaders headers = response.getHeaders();
    //             headers.add("Access-Control-Allow-Headers", ALLOWED_HEADERS);
    //             headers.add("Access-Control-Allow-Methods", ALLOWED_METHODS);
    //             headers.add("Access-Control-Allow-Origin", ALLOWED_ORIGIN);
    //             headers.add("Access-Control-Expose-Headers", ALLOWED_EXPOSE);
    //             headers.add("Access-Control-Max-Age", MAX_AGE);
    //             headers.add("Access-Control-Allow-Credentials", "true");
    //             if (request.getMethod() == HttpMethod.OPTIONS)
    //             {
    //                 response.setStatusCode(HttpStatus.OK);
    //                 return Mono.empty();
    //             }
    //         }
    //         return chain.filter(ctx);
    //     };
    // }

    @Bean
    public WebFilter corsFilter() {
        return (ServerWebExchange ctx, WebFilterChain chain) -> {
            ServerHttpRequest request = ctx.getRequest();
            if (CorsUtils.isCorsRequest(request)) {
                ServerHttpResponse response = ctx.getResponse();
                HttpHeaders headers = response.getHeaders();

                // 获取请求的来源
                String origin = request.getHeaders().getOrigin();

                // 只允许来自指定域的跨域请求
                if ("http://localhost:8080".equals(origin) || "http://localhost:8081".equals(origin)) {
                    headers.add("Access-Control-Allow-Origin", origin);  // 设置允许的来源
                } else {
                    headers.add("Access-Control-Allow-Origin", "http://localhost:8080");  // 默认值
                }

                headers.add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
                headers.add("Access-Control-Allow-Headers", "Content-Type, user-authorization, admin-authorization,from,X-Refresh-Token,istoken,repeatsubmit");
                headers.add("Access-Control-Allow-Credentials", "true");  // 允许携带 cookies
                headers.add("Access-Control-Expose-Headers", "Authorization, X-Refresh-Token");  // 需要暴露的头部

                if (request.getMethod() == HttpMethod.OPTIONS) {
                    response.setStatusCode(HttpStatus.OK);
                    return Mono.empty();  // 如果是预检请求，直接返回 200 OK
                }
            }

            return chain.filter(ctx);
        };
    }

}