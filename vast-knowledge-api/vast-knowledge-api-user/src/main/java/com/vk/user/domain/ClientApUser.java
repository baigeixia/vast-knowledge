package com.vk.user.domain;



import lombok.Data;

import java.io.Serializable;

import java.time.LocalDateTime;

/**
 * APP用户信息 实体类。
 *
 * @author 张三
 * @since 2024-05-13
 */
@Data
public class ClientApUser implements Serializable {

    /**
     * 主键
     */
    private Long id;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 头像
     */
    private String image;

    /**
     * 0:男 1:女 2:未知
     */
    private Integer sex;

    /**
     * 是否有证书认证 0:未，1:是
     */
    private Integer isCertification;

    /**
     * 0:否 1:是
     */
    private Integer isIdentityAuthentication;

    /**
     * 0:正常 1:锁定
     */
    private Integer status;

    /**
     * 0:普通用户 1:自媒体人 2:大V
     */
    private Integer flag;

    /**
     * 注册时间
     */
    private LocalDateTime createdTime;

    /**
     * 归属地
     */
    private String location;


}
