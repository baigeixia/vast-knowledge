package com.vk.user.domain;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import java.time.LocalDateTime;

/**
 * APP用户信息 实体类。
 *
 * @author 张三
 * @since 2024-05-13
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "ap_user")
public class ApUser implements Serializable {

    /**
     * 主键
     */
    @Id(keyType = KeyType.Auto)
    private Long id;

    /**
     * 密码、通信等加密盐
     */
    private String salt;

    /**
     * 用户名
     */
    private String name;

    /**
     * 密码,md5加密
     */
    private String password;

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

}
