package com.vk.common.core.constant;

/**
 * Token的Key常量
 * 
 * @author vk
 */
public class TokenConstants
{
    /**
     * 令牌自定义标识
     */
    public static final String USER_AUTHORIZATION_HEADER = "user-authorization";
    public static final String ADMIN_AUTHORIZATION_HEADER = "admin-authorization";
    public static final String REFRESH_ADMIN_TOKEN = "admin-refresh_token";
    public static final String REFRESH_TOKEN = "refresh_token";
    public static final String REFRESH_USER_TOKEN = "user-refresh_token";
    /**
     * 令牌前缀
     */
    public static final String PREFIX = "Bearer ";
    /**
     * 作者
     */
    public static final String SUBJECT = "zhangsan";

    /**
     * 令牌秘钥
     */
    public final static String SECRET = "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz";

    /**
     * TOKEN的有效期一天 s 1小时
     */
    public final static long  TOKEN_TIME_OUT = 60 * 60 * 1000; // 1小时

    /**
     *最小刷新间隔(S) 2天
     */
    public static final long  REFRESH_TIME = 2 * 24 * 60 * 60 * 1000; //2天

}
