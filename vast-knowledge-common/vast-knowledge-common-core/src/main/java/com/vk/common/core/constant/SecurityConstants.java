package com.vk.common.core.constant;

/**
 * 权限相关通用常量
 * 
 * @author vk
 */
public class SecurityConstants
{
    /**
     * 用户ID字段
     */
    public static final String DETAILS_USER_ID = "user_id";

    /**
     * 用户名字段
     */
    public static final String DETAILS_USERNAME = "username";
    public static final String ADMIN_PATH = "/dev-system/system/**";
    /**
     * 是否是管理端
     */
    public static final String ADMIN_OPEN= "adminOpen";
    /**
     * 授权信息字段
     */
    public static final String USER_AUTHORIZATION_HEADER = "user-authorization";
    public static final String ADMIN_AUTHORIZATION_HEADER = "admin-authorization";


    /**
     * 请求来源
     */
    public static final String FROM_SOURCE = "from-source";

    /**
     * 内部请求
     */
    public static final String INNER = "inner";

    /**
     * 用户标识
     */
    public static final String USER_KEY = "user_key";


    /**
     * 登录用户
     */
    public static final String LOGIN_USER = "login_user";

    /**
     * 登录管理
     */
    public static final String LOGIN_ADMIN = "login_admin";


    /**
     * 角色权限
     */
    public static final String ROLE_PERMISSION = "role_permission";
}
