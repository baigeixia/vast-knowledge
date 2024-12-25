package com.vk.common.security.utils;


import com.vk.common.core.constant.SecurityConstants;
import com.vk.common.core.constant.TokenConstants;
import com.vk.common.core.context.SecurityContextHolder;
import com.vk.common.core.utils.ServletUtils;
import com.vk.common.core.utils.StringUtils;
import com.vk.system.model.LoginUser;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Predicate;


/**
 * 权限获取工具类
 * 
 * @author vk
 */
public class SecurityUtils
{
    /**
     * 获取用户ID
     */
    public static Long getUserId()
    {
        return SecurityContextHolder.getUserId();
    }

    /**
     * 获取用户名称
     */
    public static String getUsername()
    {
        return SecurityContextHolder.getUserName();
    }

    /**
     * 获取用户key
     */
    public static String getUserKey()
    {
        return SecurityContextHolder.getUserKey();
    }

    /**
     * 获取登录 管理 用户信息
     */
    public static LoginUser getLoginUser()
    {
        return SecurityContextHolder.get(SecurityConstants.LOGIN_ADMIN, LoginUser.class);
    }

    /**
     * 获取请求token
     */
    public static String getToken()
    {
        return getToken(Objects.requireNonNull(ServletUtils.getRequest()));
    }

    /**
     * 根据request获取请求token
     */
    public static String getToken(HttpServletRequest request)
    {
        String headerName = verificationAdmin(request) ? TokenConstants.ADMIN_AUTHORIZATION_HEADER : TokenConstants.USER_AUTHORIZATION_HEADER;

        String token = request.getHeader(headerName);

        return replaceTokenPrefix(token);
    }

    public static String getRefreshToken(HttpServletRequest request)
    {
        Cookie[] cookies = request.getCookies();
        // 如果 cookies 为 null 或者长度为 0，则返回 null
        if (cookies == null) {
            return null;
        }

        for (Cookie cookie : cookies) {
            if (TokenConstants.REFRESH_TOKEN.equals(cookie.getName())) {
                // 找到对应名称的 cookie，返回其值
                return   replaceTokenPrefix(cookie.getValue());
            }
        }
        return null;
    }
    public static boolean verificationAdmin(HttpServletRequest request){
        //从header获取admin标识
        String adminOpen = request.getHeader(SecurityConstants.ADMIN_OPEN);
        return Boolean.parseBoolean(adminOpen);
    }

    /**
     * 裁剪token前缀
     */
    public static String replaceTokenPrefix(String token)
    {
        // 如果前端设置了令牌前缀，则裁剪掉前缀
        if (StringUtils.isNotEmpty(token) && token.startsWith(TokenConstants.PREFIX))
        {
            token = token.replaceFirst(TokenConstants.PREFIX, "");
        }
        return token;
    }

    /**
     * 是否为管理员
     * 
     * @param userId 用户ID
     * @return 结果
     */
    public static boolean isAdmin(Long userId)
    {
        return userId != null && 1L == userId;
    }

    /**
     * 生成BCryptPasswordEncoder密码
     *
     * @param password 密码
     * @return 加密字符串
     */
    public static String encryptPassword(String password)
    {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    /**
     * 生成BCryptPasswordEncoder密码  客户端使用
     *
     * @param password 密码
     * @return 加密字符串
     */
    public static String encryptPassword(String password,String salt)
    {
        String passwordWithSalt = password + salt;

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(passwordWithSalt);
    }

    /**
     * 判断密码是否相同
     *
     * @param rawPassword 真实密码
     * @param encodedPassword 加密后字符
     * @return 结果
     */
    public static boolean matchesPassword(String rawPassword, String encodedPassword)
    {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
