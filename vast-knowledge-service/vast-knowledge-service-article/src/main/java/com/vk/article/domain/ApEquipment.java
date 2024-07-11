package com.vk.article.domain;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDateTime;

/**
 * APP设备信息 实体类。
 *
 * @author 张三
 * @since 2024-07-11
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "ap_equipment")
public class ApEquipment implements Serializable {

    @Id(keyType = KeyType.Auto)
    private BigInteger id;

    /**
     * 0:PC 1:Android 2:IOS 3:PAD 9:其他
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
