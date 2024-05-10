package com.vk.gateway.filter;

import com.vk.gateway.config.properties.CaptchaProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author: 解决 body 流不能重复读问题
 * CacheBodyGlobalFilterk的作用是为了解决
 * ServerHttpRequest中body的数据为NULL的情况
 */
@Component
public class CacheBodyGlobalFilter implements Ordered, GlobalFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        HttpMethod method = exchange.getRequest().getMethod();
        if (method == HttpMethod.GET || method == HttpMethod.DELETE)
        {
            return chain.filter(exchange);
        }
        if (exchange.getRequest().getHeaders().getContentType() == null) {
            return chain.filter(exchange);
        } else {
            return
                    ServerWebExchangeUtils.cacheRequestBodyAndRequest(exchange, (serverHttpRequest) -> {
                        if (serverHttpRequest == exchange.getRequest()) {
                            return chain.filter(exchange);
                        }
                        return chain.filter(exchange.mutate().request(serverHttpRequest).build());
                    });
           }
        }

    //尽可能早的对这个请求进行封装
    @Override
    public int getOrder() {
        return -4;
    }



}
