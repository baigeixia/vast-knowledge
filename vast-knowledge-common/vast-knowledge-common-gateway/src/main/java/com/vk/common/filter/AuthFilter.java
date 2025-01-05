package com.vk.common.filter;


import com.vk.common.core.constant.CacheConstants;
import com.vk.common.core.constant.HttpStatus;
import com.vk.common.core.constant.SecurityConstants;
import com.vk.common.core.constant.TokenConstants;
import com.vk.common.core.context.SecurityContextHolder;
import com.vk.common.core.utils.ServletUtils;
import com.vk.common.core.utils.StringUtils;
import com.vk.common.core.utils.TokenUtils;
import com.vk.common.redis.service.RedisService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;

import static com.vk.common.core.constant.CacheConstants.LOGIN_TOKEN_KEY;


/**
 * 网关鉴权
 *
 * @author vk
 */
@Component
public class AuthFilter implements GlobalFilter, Ordered {
    private static final Logger log = LoggerFactory.getLogger(AuthFilter.class);

    // 排除过滤的 uri 地址，nacos自行添加
    @Autowired
    private IgnoreWhiteProperties ignoreWhite;

    @Autowired
    private RedisService redisService;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpRequest.Builder mutate = request.mutate();

        String url = request.getURI().getPath();

        // 跳过不需要验证的路径
        if (StringUtils.matches(url, ignoreWhite.getWhites())) {
            return chain.filter(exchange);
        }

        String token = getToken(request);
        if (StringUtils.isEmpty(token)) {
            return unauthorizedResponse(exchange, "令牌不能为空");
        }

        Claims claims = TokenUtils.parseToken(token);
        if (claims == null) {
            // 主令牌过期，是否 刷新
            return refreshToken(exchange, request);
        }

        String userid = TokenUtils.getUserId(claims);
        String username = TokenUtils.getUserName(claims);
        if (StringUtils.isEmpty(userid) || StringUtils.isEmpty(username)) {
            return refreshToken(exchange, request);
        }

        String userkey = TokenUtils.getUserKey(token);
        boolean islogin = redisService.hasKey(getTokenKey(userkey));
        if (!islogin) {
            return unauthorizedResponse(exchange, "登录状态已过期");
        }

        String userType = TokenUtils.getUserType(token);

        // 设置用户信息到请求
        addHeader(mutate, SecurityConstants.USER_KEY, userkey);
        addHeader(mutate, SecurityConstants.USER_TYPE, userType);
        addHeader(mutate, SecurityConstants.DETAILS_USER_ID, userid);
        addHeader(mutate, SecurityConstants.DETAILS_USERNAME, username);

        // 内部请求来源参数清除
        removeHeader(mutate, SecurityConstants.FROM_SOURCE);
        return chain.filter(exchange.mutate().request(mutate.build()).build());
    }

    private Mono<Void> refreshToken(ServerWebExchange exchange, ServerHttpRequest request) {
        String refreshToken = getRefreshToken(request);
        if (StringUtils.isEmpty(refreshToken)) {
            return unauthorizedResponse(exchange, "令牌已过期或验证不正确！");
        }
        Claims refresh = TokenUtils.parseToken(refreshToken);
        if (refresh == null) {
            return unauthorizedResponse(exchange, "刷新令牌已过期或无效！");
        }
        return tokenRefreshResponse(exchange, "重新刷新令牌");
    }
    private String getTokenKey(String token) {
        return LOGIN_TOKEN_KEY + token;
    }


    /**
     * 获取请求 access token
     */
    private String getToken(ServerHttpRequest request) {
        // 获取 token
        String token = request.getHeaders().getFirst(TokenConstants.AUTHORIZATION_HEADER);

        // 如果 token 存在且不为空，移除前缀并返回
        return StringUtils.isNotEmpty(token) ? token.replaceFirst(TokenConstants.PREFIX, "") : null;
    }

    private String getRefreshToken(ServerHttpRequest request) {
        MultiValueMap<String, HttpCookie> cookies = request.getCookies();
        // 检查是否包含 refresh_token
        if (cookies.containsKey(TokenConstants.REFRESH_TOKEN)) {
            // 返回 refresh_token 的值
            HttpCookie refreshTokenCookie = cookies.getFirst(TokenConstants.REFRESH_TOKEN);
            // 确保 refreshTokenCookie 不为 null
            if (refreshTokenCookie != null) {
                return refreshTokenCookie.getValue();
            }
        }
        return null;
    }

    private void addHeader(ServerHttpRequest.Builder mutate, String name, Object value) {
        if (value == null) {
            return;
        }
        String valueStr = value.toString();
            String valueEncode = ServletUtils.urlEncode(valueStr);
        mutate.header(name, valueEncode);
    }

    private void removeHeader(ServerHttpRequest.Builder mutate, String name) {
        mutate.headers(httpHeaders -> httpHeaders.remove(name)).build();
    }

    private Mono<Void> unauthorizedResponse(ServerWebExchange exchange, String msg) {
        log.error("[鉴权异常处理]请求路径:{}", exchange.getRequest().getPath());
        return ServletUtils.webFluxResponseWriter(exchange.getResponse(), msg, HttpStatus.UNAUTHORIZED);
    }

    private Mono<Void> tokenRefreshResponse(ServerWebExchange exchange, String msg) {
        log.error("[刷新令牌]请求路径:{}", exchange.getRequest().getPath());
        return ServletUtils.webFluxResponseWriter(exchange.getResponse(), msg, HttpStatus.BAD_REQUEST);
    }




    @Override
    public int getOrder() {
        return -2;
    }
}