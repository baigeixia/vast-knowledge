package com.vk.user.model;

import com.vk.user.domain.ClientApUser;
import lombok.Data;

@Data
public class LoginApUser {
    /**
     * 用户信息
     */
    private ClientApUser clientApUser;

    /**
     * 用户唯一标识
     */
    private String token;

    /**
     * 用户名id
     */
    private Long userid;
    /**
     * 用户名称
     */
    private String username;

    /**
     * 登录时间
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

}
