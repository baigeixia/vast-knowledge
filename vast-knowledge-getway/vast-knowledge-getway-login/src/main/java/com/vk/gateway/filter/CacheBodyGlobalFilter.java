package com.vk.gateway.filter;

import com.vk.common.core.utils.StringUtils;
import com.vk.gateway.config.properties.CaptchaProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author: Blossom
 * CacheBodyGlobalFilterk的作用是为了解决
 * ServerHttpRequest中body的数据为NULL的情况
 */
@Component
public class CacheBodyGlobalFilter implements Ordered, GlobalFilter {

    private final static String[] VALIDATE_URL = new String[]{"/dev-api/auth/login", "/dev-api/auth/register"};

    @Autowired
    private CaptchaProperties captchaProperties;
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        if (!StringUtils.equalsAnyIgnoreCase(request.getURI().getPath(), VALIDATE_URL) || !captchaProperties.getEnabled()) {
            return chain.filter(exchange);
        }
        if (exchange.getRequest().getHeaders().getContentType() == null) {
            return chain.filter(exchange);
        } else {
            //获取databuffer
            return
                    DataBufferUtils.join(exchange.getRequest().getBody())
                    .flatMap(dataBuffer -> { //设定返回值并处理
                        DataBufferUtils.retain(dataBuffer); //设定存储空间
                        Flux<DataBuffer> cachedFlux = Flux//读取Flux中所有数据并且保存
                                .defer(() -> Flux.just(dataBuffer.slice(0, dataBuffer.readableByteCount())));
                        ServerHttpRequest mutatedRequest = new ServerHttpRequestDecorator( //得到ServerHttpRequest
                                exchange.getRequest()) {
                            @Override //重载getBody方法 让其从我设定的缓存获取
                            public Flux<DataBuffer> getBody() {
                                return cachedFlux;
                            }
                        };
                        //放行 并且设定exchange为我重载后的
                        return chain.filter(exchange.mutate().request(mutatedRequest).build());
                    });
        }
    }
    //尽可能早的对这个请求进行封装
    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

}
