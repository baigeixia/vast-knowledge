package com.vk.common.security.service;


import com.vk.common.core.context.SecurityContextHolder;
import com.vk.system.model.LoginUser;
import com.vk.user.model.LoginApUser;
import jakarta.servlet.http.HttpServletRequest;
import com.vk.common.core.constant.CacheConstants;
import com.vk.common.core.constant.SecurityConstants;
import com.vk.common.core.utils.ServletUtils;
import com.vk.common.core.utils.StringUtils;
import com.vk.common.core.utils.TokenUtils;
import com.vk.common.core.utils.ip.IpUtils;
import com.vk.common.core.utils.uuid.IdUtils;
import com.vk.common.redis.service.RedisService;
import com.vk.common.security.utils.SecurityUtils;

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
public class TokenService
{
    private static final Logger log = LoggerFactory.getLogger(TokenService.class);

    @Autowired
    private RedisService redisService;

    protected static final long MILLIS_SECOND = 1000;

    protected static final long MILLIS_MINUTE = 60 * MILLIS_SECOND;

    private final static long expireTime = CacheConstants.EXPIRATION;

    private final static String LOGIN_TOKEN_KEY = CacheConstants.LOGIN_TOKEN_KEY;
    private final static String LOGIN_CLIENT_TOKEN_KEY = CacheConstants.LOGIN_CLIENT_TOKEN_KEY;

    private final static Long MILLIS_MINUTE_TEN = CacheConstants.REFRESH_TIME * MILLIS_MINUTE;

    /**
     * 创建LoginUser令牌
     */
    public Map<String, Object> createToken(LoginUser loginUser) {
        return createTokenForUser(loginUser.getSysUser().getUserId(), loginUser.getSysUser().getUserName(), loginUser);
    }
    /**
     * 创建LoginApUser令牌
     */
    public Map<String, Object> createToken(LoginApUser loginUser) {
        return createTokenForUser(loginUser.getClientApUser().getId(), loginUser.getUsername(), loginUser);
    }

    private Map<String, Object> createTokenForUser(Long userId, String username, Object loginUser) {
        String token = IdUtils.fastUUID();

        // Set common token info
        if (loginUser instanceof LoginUser) {
            ((LoginUser) loginUser).setToken(token);
            ((LoginUser) loginUser).setUserid(userId);
            ((LoginUser) loginUser).setUsername(username);
            ((LoginUser) loginUser).setIpaddr(IpUtils.getIpAddr());
        } else if (loginUser instanceof LoginApUser) {
            ((LoginApUser) loginUser).setToken(token);
            ((LoginApUser) loginUser).setUserid(userId);
            ((LoginApUser) loginUser).setIpaddr(IpUtils.getIpAddr());
        }

        refreshToken(loginUser);

        // Create JWT claims
        Map<String, Object> claimsMap = new HashMap<>();
        claimsMap.put(SecurityConstants.USER_KEY, token);
        claimsMap.put(SecurityConstants.DETAILS_USER_ID, userId);
        claimsMap.put(SecurityConstants.DETAILS_USERNAME, username);

        // Return the response map with token and expiration time
        Map<String, Object> rspMap = new HashMap<>();
        rspMap.put("access_token", TokenUtils.createToken(claimsMap));
        rspMap.put("expires_in", expireTime);
        return rspMap;
    }

    /**
     * 通用的 refreshToken 方法
     *
     * @param user 登录信息
     */
    public  <T> void refreshToken(T user) {
        // 获取当前时间
        long currentTime = System.currentTimeMillis();

        // 设置登录时间和过期时间
        if (user instanceof LoginUser loginUser) {
            loginUser.setLoginTime(currentTime);
            loginUser.setExpireTime(currentTime + expireTime * MILLIS_MINUTE);
            String userKey = getTokenKey(loginUser.getToken());
            redisService.setCacheObject(userKey, loginUser, expireTime, TimeUnit.MINUTES);
        } else if (user instanceof LoginApUser loginApUser) {
            loginApUser.setLoginTime(currentTime);
            loginApUser.setExpireTime(currentTime + expireTime * MILLIS_MINUTE);
            String userKey = getTokenKey(loginApUser.getToken());
            redisService.setCacheObject(userKey, loginApUser, expireTime, TimeUnit.MINUTES);
        }
    }

    /**
     * 获取用户身份信息
     *
     * @return 用户信息
     */
    public LoginUser getLoginUser()
    {
        return getLoginUser(ServletUtils.getRequest());
    }

    /**
     * 获取用户身份信息
     *
     * @return 用户信息
     */
    public LoginUser getLoginUser(HttpServletRequest request)
    {
        // 获取请求携带的令牌
        String token = SecurityUtils.getToken(request);
        return getLoginUser(token);
    }

    /**
     * 获取用户身份信息
     *
     * @return 用户信息
     */
    public LoginUser getLoginUser(String token)
    {
        LoginUser user = null;
        try
        {
            if (StringUtils.isNotEmpty(token))
            {
                String userkey = TokenUtils.getUserKey(token);
                user = redisService.getCacheObject(getTokenKey(userkey));
                return user;
            }
        }
        catch (Exception e)
        {
            log.error("获取用户信息异常'{}'", e.getMessage());
        }
        return user;
    }

    /**
     * 设置用户身份信息
     */
    public void setLoginUser(LoginUser loginUser)
    {
        if (StringUtils.isNotNull(loginUser) && StringUtils.isNotEmpty(loginUser.getToken()))
        {
            refreshToken(loginUser);
        }
    }

    /**
     * 删除用户缓存信息
     */
    public void delLoginUser(String token)
    {
        if (StringUtils.isNotEmpty(token))
        {
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
        if (expireTime - currentTime <= MILLIS_MINUTE_TEN) {
            refreshToken(user);
        }
    }


    private String getTokenKey(String token)
    {
        String adminInfo = SecurityContextHolder.get(SecurityConstants.ADMIN_OPEN);
        if (Boolean.parseBoolean(adminInfo)){
            return LOGIN_TOKEN_KEY + token;
        }else {
            return LOGIN_CLIENT_TOKEN_KEY + token;
        }
    }
}