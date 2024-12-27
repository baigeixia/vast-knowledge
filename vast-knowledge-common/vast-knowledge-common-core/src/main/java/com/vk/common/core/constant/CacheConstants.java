package com.vk.common.core.constant;

/**
 * 缓存常量信息
 * 
 * @author vk
 */
public class CacheConstants
{
    /**
     * 缓存有效期，默认720（分钟）
     */
    public final static long EXPIRATION = 720;

    /**
     * 缓存刷新时间，默认120（分钟）
     */
    public final static long REFRESH_TIME = 120;

    /**
     * 密码最大错误次数
     */
    public final static int PASSWORD_MAX_RETRY_COUNT = 5;

    /**
     * 密码锁定时间，默认10（分钟）
     */
    public final static long PASSWORD_LOCK_TIME = 10;

    /**
     * 管理端 缓存
     */
    public final static String LOGIN_TOKEN_KEY = "login_tokens:";

    /**
     * 用户 缓存
     */
    public final static String LOGIN_CLIENT_TOKEN_KEY = "login_client_tokens:";

    /**
     * 验证码 redis key
     */
    public static final String CAPTCHA_CODE_KEY = "captcha_codes:";
    /**
     * 页面唯一token
     */
    public static final String PAGE_TOKEN = "page_token:";
    /**
     * 缓存时间（秒）
     */
    public static final long PAGE_TOKEN_TIME = 1;

    /**
     * 参数管理 cache key
     */
    public static final String SYS_CONFIG_KEY = "sys_config:";

    /**
     * 字典管理 cache key
     */
    public static final String SYS_DICT_KEY = "sys_dict:";

    /**
     * 登录账户密码错误次数 redis key
     */
    public static final String PWD_ERR_CNT_KEY = "pwd_err_cnt:";

    /**
     * 登录IP黑名单 cache key
     */
    public static final String SYS_LOGIN_BLACKIPLIST = SYS_CONFIG_KEY + "sys.login.blackIPList";

    /**
     * 字典管理 cache key
     */
    public static final String AD_CHANNEL_KEY = "ad_channel:";

    /**
     * 字典管理 cache key
     */
    public static final String AD_CHANNEL_LABEL_KEY = "ad_channel_label:";

}
