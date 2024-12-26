package com.vk.common.security.service;


import com.vk.common.core.constant.CacheConstants;
import com.vk.common.core.constant.SecurityConstants;
import com.vk.common.core.constant.TokenConstants;
import com.vk.common.core.context.SecurityContextHolder;
import com.vk.common.core.utils.ServletUtils;
import com.vk.common.core.utils.StringUtils;
import com.vk.common.core.utils.TokenUtils;
import com.vk.common.core.utils.ip.IpUtils;
import com.vk.common.core.utils.uuid.IdUtils;
import com.vk.common.redis.service.RedisService;
import com.vk.common.security.utils.SecurityUtils;
import com.vk.system.model.LoginUser;
import com.vk.user.model.LoginApUser;
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
import java.util.Optional;
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
    private final static String LOGIN_CLIENT_TOKEN_KEY = CacheConstants.LOGIN_CLIENT_TOKEN_KEY;

    private final static Long MILLIS_MINUTE_TEN = TokenConstants.REFRESH_TIME;

    /**
     * 创建LoginUser令牌
     */
    public Map<String, Object> createToken(LoginUser loginUser, HttpServletResponse response) {
        return createTokenForUser(loginUser.getSysUser().getUserId(), loginUser.getSysUser().getUserName(), loginUser, response);
    }

    /**
     * 创建LoginApUser令牌
     */
    public Map<String, Object> createToken(LoginApUser loginUser, HttpServletResponse response) {
        return createTokenForUser(loginUser.getClientApUser().getId(), loginUser.getUsername(), loginUser, response);
    }

    private Map<String, Object> createTokenForUser(Long userId, String username, Object loginUser, HttpServletResponse response) {
        String key = IdUtils.fastUUID();

        // Set common token info
        if (loginUser instanceof LoginUser) {
            ((LoginUser) loginUser).setToken(key);
            ((LoginUser) loginUser).setUserid(userId);
            ((LoginUser) loginUser).setUsername(username);
            ((LoginUser) loginUser).setIpaddr(IpUtils.getIpAddr());
        } else if (loginUser instanceof LoginApUser) {
            ((LoginApUser) loginUser).setToken(key);
            ((LoginApUser) loginUser).setUserid(userId);
            ((LoginApUser) loginUser).setIpaddr(IpUtils.getIpAddr());
        }

        refreshTokenRedis(loginUser);
        refreshToken(response, key);
        String token = createToken(key, userId, username);

        // Return the response map with token and expiration time
        Map<String, Object> rspMap = new HashMap<>();
        rspMap.put("access_token", token);
        // rspMap.put("refresh_token", TokenUtils.createRefreshToken(claimsMap));
        rspMap.put("expires_in", TokenConstants.REFRESH_TIME);
        return rspMap;
    }

    public String createToken(String key, Long userId, String username) {
        Map<String, Object> claimsMap = new HashMap<>();
        claimsMap.put(SecurityConstants.USER_KEY, key);
        claimsMap.put(SecurityConstants.DETAILS_USER_ID, userId);
        claimsMap.put(SecurityConstants.DETAILS_USERNAME, username);
        return TokenUtils.createToken(claimsMap);
    }


    public void refreshToken(HttpServletResponse response, String key) {
        String refreshToken = createRefreshToken(key);
        addRefreshToken(response, refreshToken);
    }


    /**
     * 生成 Refresh token
     *
     * @param key
     * @return
     */
    private String createRefreshToken(String key) {
        Map<String, Object> claimsMap = new HashMap<>();
        claimsMap.put(SecurityConstants.USER_KEY, key);
        return TokenUtils.createRefreshToken(claimsMap);
    }

    /**
     * 添加 Refresh token
     *
     * @param
     * @return
     */
    private void addRefreshToken(HttpServletResponse response, String refreshToken) {
        Cookie refreshTokenCookie = new Cookie("refresh_token", refreshToken);
        refreshTokenCookie.setHttpOnly(true);  // Prevent client-side access to the cookie
        refreshTokenCookie.setSecure(true);    // Ensure it's only sent over HTTPS
        refreshTokenCookie.setAttribute("SameSite", "None");
        refreshTokenCookie.setPath("/");  // Accessible throughout the application
        refreshTokenCookie.setMaxAge((int) (TokenConstants.REFRESH_TIME / 1000));  // Set expiration time (in seconds)
        // Add the cookie to the response
        response.addCookie(refreshTokenCookie);
    }

    public void refreshTokenWeb(String key) {
        // String refreshToken = createRefreshToken(token);
        HttpServletResponse response = ServletUtils.getResponse();
        if (!ObjectUtils.isEmpty(response)) {
            addRefreshToken(response, createRefreshToken(key));
        }
    }


    /**
     * 通用的 refreshTokenRedis 方法
     *
     * @param user 登录信息
     */
    public <T> void refreshTokenRedis(T user) {
        refreshTokenInRedis(user);
    }

    private <T> void refreshTokenInRedis(T user) {
        // 获取当前时间
        long currentTime = System.currentTimeMillis();
        // 设置登录时间和过期时间
        if (user instanceof LoginUser loginUser) {
            loginUser.setLoginTime(currentTime);
            loginUser.setExpireTime(currentTime + EXPIRE_TIME);
            String key = loginUser.getToken();
            String userKey = getTokenKey(key);
            redisService.setCacheObject(userKey, loginUser, EXPIRE_TIME / MILLIS_MINUTE, TimeUnit.MINUTES);
        } else if (user instanceof LoginApUser loginApUser) {
            loginApUser.setLoginTime(currentTime);
            loginApUser.setExpireTime(currentTime + EXPIRE_TIME);
            String key = loginApUser.getToken();
            String userKey = getTokenKey(key);
            redisService.setCacheObject(userKey, loginApUser, EXPIRE_TIME / MILLIS_MINUTE, TimeUnit.MINUTES);
        }
    }


    /**
     * 获取用户身份信息
     *
     * @return 用户信息
     */
    public LoginUser getLoginUser() {
        return getLoginUser(ServletUtils.getRequest());
    }

    /**
     * 获取用户身份信息
     *
     * @return 用户信息
     */
    public LoginUser getLoginUser(HttpServletRequest request) {
        // 获取请求携带的令牌
        String token = SecurityUtils.getRefreshToken(request);
        return getLoginUserInfo(token);
    }


    /**
     * 设置用户身份信息
     */
    public void setLoginUser(LoginUser loginUser) {
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
    public LoginApUser getLoginApUser() {
        return getLoginApUser(ServletUtils.getRequest());
    }

    /**
     * 获取用户身份信息
     *
     * @return 用户信息
     */
    public LoginApUser getLoginApUser(HttpServletRequest request) {
        // 获取请求携带的令牌
        // String token = SecurityUtils.getToken(request);
        // LoginApUser user= getLoginUserInfo(token);
        // if(ObjectUtils.isEmpty(user)){
        //
        // }
        // return user;
        String refreshToken = SecurityUtils.getRefreshToken(request);
        return getLoginUserInfo(refreshToken);
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

    /**
     * 设置用户身份信息
     */
    public void setLoginApUser(LoginApUser loginApUser) {
        if (StringUtils.isNotNull(loginApUser) && StringUtils.isNotEmpty(loginApUser.getToken())) {
            refreshTokenRedis(loginApUser);
        }
    }

    /**
     * 删除用户缓存信息
     */
    public void delLoginApUser(String token) {
        if (StringUtils.isNotEmpty(token)) {
            String userkey = TokenUtils.getUserKey(token);
            redisService.deleteObject(getTokenKey(userkey));
        }
    }


    /**
     * 验证令牌有效期，相差不足120分钟，自动刷新缓存
     *
     * @param
     */
    public <T> void verifyToken(T user) {
        long expireTime = 0;
        // 获取用户的过期时间
        if (user instanceof LoginUser loginUser) {
            expireTime = loginUser.getExpireTime();
        } else if (user instanceof LoginApUser loginApUser) {
            expireTime = loginApUser.getExpireTime();
        }

        long currentTime = System.currentTimeMillis();
        if (expireTime - currentTime <= EXPIRE_TIME) {
            refreshTokenRedis(user);
        }
    }


    public <T> void verifyTokenAndRefreshToken(T user) {
        long expireTime = 0;
        String key = null;
        // 获取用户的过期时间
        if (user instanceof LoginUser loginUser) {
            expireTime = loginUser.getExpireTime();

            key = loginUser.getToken();
        } else if (user instanceof LoginApUser loginApUser) {
            expireTime = loginApUser.getExpireTime();
            key = loginApUser.getToken();
        }

        long currentTime = System.currentTimeMillis();
        if (expireTime - currentTime <= EXPIRE_TIME) {
            HttpServletResponse response = ServletUtils.getResponse();
            if (!ObjectUtils.isEmpty(response)) {
                refreshTokenRedis(user);
                refreshToken(response, key);
            }
        }
    }


    private String getTokenKey(String token) {
        String isAdmin = SecurityContextHolder.get(SecurityConstants.ADMIN_OPEN);
        boolean isAdminFlag = Optional.ofNullable(isAdmin).map(Boolean::parseBoolean).orElse(false);
        return isAdminFlag ? LOGIN_TOKEN_KEY + token : LOGIN_CLIENT_TOKEN_KEY + token;
    }
}