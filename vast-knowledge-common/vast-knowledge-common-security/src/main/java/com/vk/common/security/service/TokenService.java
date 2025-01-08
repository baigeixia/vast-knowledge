package com.vk.common.security.service;


import com.vk.common.core.constant.CacheConstants;
import com.vk.common.core.constant.SecurityConstants;
import com.vk.common.core.constant.TokenConstants;
import com.vk.common.core.constant.VisitorStatisticsConstant;
import com.vk.common.core.utils.ServletUtils;
import com.vk.common.core.utils.StringUtils;
import com.vk.common.core.utils.TokenUtils;
import com.vk.common.core.utils.ip.IpUtils;
import com.vk.common.core.utils.uuid.IdUtils;
import com.vk.common.redis.service.RedisService;
import com.vk.common.security.utils.SecurityUtils;
import com.vk.system.model.LoginUser;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * token验证处理
 *
 * @author vk
 */
@Component
public class TokenService {
    private static final Logger log = LoggerFactory.getLogger(TokenService.class);

    @Autowired
    private RedisService redisService;


    protected static final long MILLIS_SECOND = 1000;

    protected static final long MILLIS_MINUTE = 60 * MILLIS_SECOND;

    private final static long EXPIRE_TIME = TokenConstants.REFRESH_TIME;

    private final static String LOGIN_TOKEN_KEY = CacheConstants.LOGIN_TOKEN_KEY;

    private final static Long MILLIS_MINUTE_TEN = TokenConstants.REFRESH_TIME;

    /**
     * 创建LoginUser令牌
     */
    public <T> Map<String, Object> createToken(LoginUser<T> loginUser, HttpServletRequest request, HttpServletResponse response) {
        String key = IdUtils.fastUUID();

        String markType = loginUser.getMarkType();
        loginUser.setToken(key);
        loginUser.setIpaddr(IpUtils.getIpAddr());

        refreshTokenRedis(loginUser);

        addRefreshToken(request, response, markType, key);

        String token = createToken(key, loginUser.getUserid(), loginUser.getUsername(),markType);

        Map<String, Object> rspMap = new HashMap<>();
        rspMap.put("access_token", token);
        rspMap.put("expires_in", TokenConstants.TOKEN_TIME_OUT);
        return rspMap;
    }


    public String createToken(String key, Long userId, String username,String markType) {
        try {
            redisService.setBit(VisitorStatisticsConstant.getVisitorDauKey(),userId);
            redisService.incr(VisitorStatisticsConstant.getVisitorPvKey());
        } catch (Exception e) {
            log.error("redis add DAU PV error:{}",e.getMessage());
        }

        Map<String, Object> claimsMap = new HashMap<>();
        claimsMap.put(SecurityConstants.USER_KEY, key);
        claimsMap.put(SecurityConstants.USER_TYPE, markType);
        claimsMap.put(SecurityConstants.DETAILS_USER_ID, userId);
        claimsMap.put(SecurityConstants.DETAILS_USERNAME, username);
        return TokenUtils.createToken(claimsMap);
    }


    /**
     * 添加 Refresh token
     *
     * @param
     * @return
     */
    public void addRefreshToken(HttpServletRequest request, HttpServletResponse response, String type, String key) {
        Map<String, Object> claims = new HashMap<>();

        String token = SecurityUtils.getRefreshToken(request);
        if (StringUtils.isNotEmpty(token)) {
            Claims parseToken = TokenUtils.parseToken(token);
            if (!ObjectUtils.isEmpty(parseToken)) {
                claims.putAll(parseToken);
            }
        }

        claims.put(type, key);
        String refreshToken = TokenUtils.createRefreshToken(claims);

        Cookie refreshTokenCookie = new Cookie(TokenConstants.REFRESH_TOKEN, refreshToken);
        refreshTokenCookie.setHttpOnly(true); // 设置 HttpOnly，防止 JavaScript 访问
        refreshTokenCookie.setSecure(true); // 设置 Secure，表示只有 HTTPS 请求才可以传输
        refreshTokenCookie.setAttribute("SameSite", "None"); // 设置 SameSite 属性为 None，允许跨站请求
        refreshTokenCookie.setPath("/");// 设置 Cookie 对应的路径
        refreshTokenCookie.setMaxAge((int) (TokenConstants.REFRESH_TIME/1000)); // 设置过期时间为 2 天
        response.addCookie(refreshTokenCookie); // 添加 Cookie 到响应中
    }


    /**
     * 通用的 refreshTokenRedis 方法
     *
     * @param user 登录信息
     */
    public <T> void refreshTokenRedis(LoginUser<T> user) {
        refreshTokenInRedis(user);
    }

    private <T> void refreshTokenInRedis(LoginUser<T> user) {
        // 获取当前时间
        long currentTime = System.currentTimeMillis();

        user.setLoginTime(currentTime);
        user.setExpireTime(currentTime + EXPIRE_TIME);
        String key = user.getToken();
        String userKey = getTokenKey(key);

        redisService.setCacheObject(userKey, user, EXPIRE_TIME / MILLIS_MINUTE, TimeUnit.MINUTES);
    }

    /**
     * 获取用户身份信息
     *
     * @return 用户信息
     */
    public <T> LoginUser<T> getLoginUser(HttpServletRequest request,String type) {
        // 获取请求携带的令牌
        String token = SecurityUtils.getRefreshToken(request);
        LoginUser<T> user = null;
        try {
            if (StringUtils.isNotEmpty(token)) {
                String userkey = TokenUtils.getRefreshUserKey(token,type);
                user = redisService.getCacheObject(getTokenKey(userkey));  // 自动转换为泛型类型 T
            }
        } catch (Exception e) {
            log.error("获取用户信息异常: '{}'", e.getMessage());
        }
        return user;
    }


    /**
     * 设置用户身份信息
     */
    public <T> void setLoginUser(LoginUser<T> loginUser) {
        if (StringUtils.isNotNull(loginUser) && StringUtils.isNotEmpty(loginUser.getToken())) {
            refreshTokenRedis(loginUser);
        }
    }

    /**
     * 删除用户缓存信息
     */
    public void delLoginUser(String token) {
        if (StringUtils.isNotEmpty(token)) {
            String userkey = TokenUtils.getUserKey(token);
            redisService.deleteObject(getTokenKey(userkey));
        }
    }


    /**
     * 获取用户身份信息
     *
     * @return 用户信息
     */
    public <T> T getLoginUserInfo(String token) {
        T user = null;
        try {
            if (StringUtils.isNotEmpty(token)) {
                String userkey = TokenUtils.getUserKey(token);
                user = redisService.getCacheObject(getTokenKey(userkey));  // 自动转换为泛型类型 T
            }
        } catch (Exception e) {
            log.error("获取用户信息异常: '{}'", e.getMessage());
        }
        return user;
    }


    public <T> void verifyTokenAndRefreshToken(LoginUser<T> user) {
        // 获取用户的过期时间
        long  expireTime = user.getExpireTime();
        String markType = user.getMarkType();
        String key = user.getToken();

        long currentTime = System.currentTimeMillis();
        if (expireTime - currentTime <= EXPIRE_TIME) {
            HttpServletResponse response = ServletUtils.getResponse();
            HttpServletRequest request = ServletUtils.getRequest();
            if (!ObjectUtils.isEmpty(response) && !ObjectUtils.isEmpty(request)) {
                refreshTokenRedis(user);
                addRefreshToken(request,response, markType, key);
            }
        }
    }

    private String getTokenKey(String token) {
        return LOGIN_TOKEN_KEY + token;
    }
}