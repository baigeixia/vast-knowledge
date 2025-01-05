package com.vk.system.model;


import com.vk.system.domain.SysUser;

import java.io.Serial;
import java.io.Serializable;
import java.util.Set;

/**
 * 用户信息
 *
 * @author vk
 */
public class LoginUser<T> implements Serializable
{
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户唯一标识
     */
    private String token;
    /**
     * 客户端标识
     */
    private String markType;

    /**
     * 用户名id
     */
    private Long userid;

    /**
     * 用户名
     */
    private String username;

    /**
     * 登录时间 也是 刷新时间点
     */
    private Long loginTime;

    /**
     * 过期时间
     */
    private Long expireTime;

    /**
     * 登录IP地址
     */
    private String ipaddr;

    /**
     * 权限列表
     */
    private Set<String> permissions;

    /**
     * 角色列表
     */
    private Set<String> rolesLocal;

    /**
     * 用户信息
     */
    private T sysUser;

    public String getToken()
    {
        return token;
    }

    public void setToken(String token)
    {
        this.token = token;
    }

    public Long getUserid()
    {
        return userid;
    }

    public String getMarkType() {
        return markType;
    }

    public void setMarkType(String markType) {
        this.markType = markType;
    }
    public void setUserid(Long userid)
    {
        this.userid = userid;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public Long getLoginTime()
    {
        return loginTime;
    }

    public void setLoginTime(Long loginTime)
    {
        this.loginTime = loginTime;
    }

    public Long getExpireTime()
    {
        return expireTime;
    }

    public void setExpireTime(Long expireTime)
    {
        this.expireTime = expireTime;
    }

    public String getIpaddr()
    {
        return ipaddr;
    }

    public void setIpaddr(String ipaddr)
    {
        this.ipaddr = ipaddr;
    }

    public Set<String> getPermissions()
    {
        return permissions;
    }

    public void setPermissions(Set<String> permissions)
    {
        this.permissions = permissions;
    }

    public Set<String> getRolesLocal()
    {
        return rolesLocal;
    }

    public void setRolesLocal(Set<String> rolesLocal)
    {
        this.rolesLocal = rolesLocal;
    }

    public T getSysUser() {
        return sysUser;
    }

    // 设置用户信息
    public void setSysUser(T sysUser) {
        this.sysUser = sysUser;
    }
}
