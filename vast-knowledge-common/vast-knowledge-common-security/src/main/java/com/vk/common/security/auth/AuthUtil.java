package com.vk.common.security.auth;


import com.vk.common.core.constant.TokenConstants;
import com.vk.common.security.annotation.RequiresPermissions;
import com.vk.common.security.annotation.RequiresRoles;
import com.vk.system.model.LoginUser;
import com.vk.user.model.LoginApUser;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Token 权限验证工具类
 * 
 * @author vk
 */
public class AuthUtil
{
    /**
     * 底层的 AuthLogic 对象
     */
    public static AuthLogic authLogic = new AuthLogic();

    /**
     * 会话注销
     */
    public static void logout()
    {
        authLogic.logout();
    }

    /**
     * 会话注销，根据指定Token
     * 
     * @param token 指定token
     */
    public static void logoutByToken(String token)
    {
        authLogic.logoutByToken(token);
    }

    /**
     * 检验当前会话是否已经登录，如未登录，则抛出异常
     */
    public static void checkLogin()
    {
        authLogic.checkLogin();
    }

    /**
     * 获取当前登录用户信息
     * 
     * @param token 指定token
     * @return 用户信息
     */
    public static LoginUser getLoginUser(String token)
    {
        return authLogic.getLoginUser(token);
    }

    public static LoginApUser getLoginApUser(String token)
    {
        return authLogic.getLoginApUser(token);
    }

    /**
     * 验证当前用户有效期
     * 
     * @param loginUser 用户信息
     */
    public static void verifyLoginUserExpire(LoginUser loginUser)
    {
        authLogic.verifyLoginUserExpire(loginUser);
    }

    public static void verifyLoginUserExpire(LoginApUser loginApUser)
    {
        authLogic.verifyLoginUserExpire(loginApUser);
    }

    /**
     * 当前账号是否含有指定角色标识, 返回true或false
     * 
     * @param role 角色标识
     * @return 是否含有指定角色标识
     */
    public static boolean hasRole(String role)
    {
        return authLogic.hasRole(role);
    }

    /**
     * 当前账号是否含有指定角色标识, 如果验证未通过，则抛出异常: NotRoleException
     * 
     * @param role 角色标识
     */
    public static void checkRole(String role)
    {
        authLogic.checkRole(role);
    }

    /**
     * 根据注解传入参数鉴权, 如果验证未通过，则抛出异常: NotRoleException
     * 
     * @param requiresRoles 角色权限注解
     */
    public static void checkRole(RequiresRoles requiresRoles)
    {
        authLogic.checkRole(requiresRoles);
    }

    /**
     * 当前账号是否含有指定角色标识 [指定多个，必须全部验证通过]
     * 
     * @param roles 角色标识数组
     */
    public static void checkRoleAnd(String... roles)
    {
        authLogic.checkRoleAnd(roles);
    }

    /**
     * 当前账号是否含有指定角色标识 [指定多个，只要其一验证通过即可]
     * 
     * @param roles 角色标识数组
     */
    public static void checkRoleOr(String... roles)
    {
        authLogic.checkRoleOr(roles);
    }

    /**
     * 当前账号是否含有指定权限, 返回true或false
     * 
     * @param permission 权限码
     * @return 是否含有指定权限
     */
    public static boolean hasPermi(String permission)
    {
        return authLogic.hasPermi(permission);
    }

    /**
     * 当前账号是否含有指定权限, 如果验证未通过，则抛出异常: NotPermissionException
     * 
     * @param permission 权限码
     */
    public static void checkPermi(String permission)
    {
        authLogic.checkPermi(permission);
    }

    /**
     * 根据注解传入参数鉴权, 如果验证未通过，则抛出异常: NotPermissionException
     * 
     * @param requiresPermissions 权限注解
     */
    public static void checkPermi(RequiresPermissions requiresPermissions)
    {
        authLogic.checkPermi(requiresPermissions);
    }

    /**
     * 当前账号是否含有指定权限 [指定多个，必须全部验证通过]
     * 
     * @param permissions 权限码数组
     */
    public static void checkPermiAnd(String... permissions)
    {
        authLogic.checkPermiAnd(permissions);
    }

    /**
     * 当前账号是否含有指定权限 [指定多个，只要其一验证通过即可]
     * 
     * @param permissions 权限码数组
     */
    public static void checkPermiOr(String... permissions)
    {
        authLogic.checkPermiOr(permissions);
    }


    public static void logoutRefreshToken(HttpServletResponse response) {
        Cookie cookie = new Cookie(TokenConstants.REFRESH_TOKEN, null);
        cookie.setPath("/");  // 设置为你应用的根路径，确保在所有路径下都可以删除
        cookie.setMaxAge(0);  // 设置过期时间为 0，表示删除此 Cookie
        cookie.setSecure(true);  // 根据需要设置 secure 属性，确保在 HTTPS 下传输
        cookie.setHttpOnly(true);  // 如果需要设置 HttpOnly，防止 JavaScript 访问该 Cookie
        cookie.setAttribute("SameSite", "None");

        response.addCookie(cookie);  // 添加这个 Cookie 到响应中
    }
}
