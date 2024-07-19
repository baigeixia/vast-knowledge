package com.vk.analyze.domain;

import java.time.LocalDateTime;

public class AdChannel {

    private Long id;

    /**
     * 频道名称
     */
    private String name;

    /**
     * 频道描述
     */
    private String icon;

    /**
     * 是否默认频道
     */
    private Integer isDefault;

    private Integer status;

    /**
     * 默认排序
     */
    private Integer ord;

    /**
     * 创建时间
     */
    private LocalDateTime createdTime;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Integer isDefault) {
        this.isDefault = isDefault;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getOrd() {
        return ord;
    }

    public void setOrd(Integer ord) {
        this.ord = ord;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }
}
