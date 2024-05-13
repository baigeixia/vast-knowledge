package com.vk.wemedia.domain;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;

/**
 * 自媒体用户登录行为信息 实体类。
 *
 * @author 张三
 * @since 2024-05-13
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "wm_user_login")
public class WmUserLogin implements Serializable {

    @Id
    private BigInteger id;

    /**
     * 用户ID
     */
    private BigInteger userId;

    /**
     * 登录设备ID
     */
    private BigInteger equipmentId;

    /**
     * 登录IP
     */
    private String ip;

    /**
     * 登录地址
     */
    private String address;

    /**
     * 经度
     */
    private BigDecimal longitude;

    /**
     * 纬度
     */
    private BigDecimal latitude;

    /**
     * 登录时间
     */
    private LocalDateTime createdTime;

}
