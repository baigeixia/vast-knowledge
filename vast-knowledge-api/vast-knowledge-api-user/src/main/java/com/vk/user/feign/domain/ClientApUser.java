package com.vk.user.feign.domain;



import java.io.Serializable;

import java.time.LocalDateTime;

/**
 * APP用户信息 实体类。
 *
 * @author 张三
 * @since 2024-05-13
 */
public class ClientApUser implements Serializable {

    /**
     * 主键
     */
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Integer getIsCertification() {
        return isCertification;
    }

    public void setIsCertification(Integer isCertification) {
        this.isCertification = isCertification;
    }

    public Integer getIsIdentityAuthentication() {
        return isIdentityAuthentication;
    }

    public void setIsIdentityAuthentication(Integer isIdentityAuthentication) {
        this.isIdentityAuthentication = isIdentityAuthentication;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }
}
