package com.vk.common.config;

import com.alibaba.fastjson2.JSON;
import com.vk.common.core.exception.CaptchaException;
import com.vk.common.core.utils.StringUtils;
import com.vk.common.core.web.domain.AjaxResult;
import com.vk.common.filter.IgnoreWhiteProperties;
import com.vk.common.service.ValidateCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class CaptchaVerificationFilter implements GlobalFilter, Ordered {

    @Autowired
    private ValidateCodeService validateCodeService;

    @Autowired
    private IgnoreWhiteProperties ignoreWhite;
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        String url = request.getURI().getPath();

        if (StringUtils.matches(url, ignoreWhite.getCheckCode())) {
            // 获取验证码和 uuid 参数
            String code = request.getHeaders().getFirst("longin-code");
            String uuid = request.getHeaders().getFirst("uuid-token");

            if (StringUtils.isEmpty(code) && StringUtils.isEmpty(uuid)){
                // return exchange.getResponse().setComplete();  // 停止请求链
                return errorAjax(response);
            }

            try {
                // 验证验证码
                validateCodeService.checkCaptcha( code,uuid);
            } catch (CaptchaException e) {
                // 如果验证码验证失败，返回错误响应
                // response.setStatusCode(HttpStatus.BAD_REQUEST);
                return errorAjax(response);
            }
        }

        // 验证通过，继续执行后续的处理
        return chain.filter(exchange);
    }

    private static Mono<Void> errorAjax(ServerHttpResponse response) {
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        DataBuffer dataBuffer = response.bufferFactory().wrap(JSON.toJSONString(AjaxResult.error(422,"验证码错误")).getBytes());
        return response.writeWith(Mono.just(dataBuffer));
    }

    @Override
    public int getOrder() {
        return -3;
    }
}
