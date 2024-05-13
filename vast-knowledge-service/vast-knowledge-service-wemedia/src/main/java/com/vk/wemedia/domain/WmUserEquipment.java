package com.vk.wemedia.domain;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDateTime;

/**
 * 自媒体用户设备信息 实体类。
 *
 * @author 张三
 * @since 2024-05-13
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "wm_user_equipment")
public class WmUserEquipment implements Serializable {

    @Id
    private BigInteger id;

    /**
     * 用户ID
     */
    private BigInteger userId;

    /**
     * 0PC 1ANDROID 2IOS 3PAD 9 其他
     */
    private Integer type;

    /**
     * 设备版本
     */
    private String version;

    /**
     * 设备系统
     */
    private String sys;

    /**
     * 设备唯一号，MD5加密
     */
    private String no;

    /**
     * 登录时间
     */
    private LocalDateTime createdTime;

}
