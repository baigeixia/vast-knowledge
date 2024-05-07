package com.vk.common.core.constant;

/**
 * Token的Key常量
 * 
 * @author ruoyi
 */
public class TokenConstants
{
    /**
     * 令牌自定义标识
     */
    public static final String AUTHENTICATION = "Authorization";

    /**
     * 令牌前缀
     */
    public static final String PREFIX = "Bearer ";
    /**
     * 作者
     */
    public static final String SUBJECT = "zhangsan ";

    /**
     * 令牌秘钥
     */
    public final static String SECRET = "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz";

    /**
     * TOKEN的有效期一天 s
     */
    public final static int TOKEN_TIME_OUT =  3600*24*100;

    /**
     *最小刷新间隔(S)
     */
    private static final int REFRESH_TIME = 300;

}
