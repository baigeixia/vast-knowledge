package com.vk.user.domain;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import java.time.LocalDateTime;

/**
 * APP用户设备信息 实体类。
 *
 * @author 张三
 * @since 2024-05-13
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "ap_user_equipment")
public class ApUserEquipment implements Serializable {

    @Id
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 设备ID
     */
    private Long equipmentId;

    /**
     * 上次使用时间
     */
    private LocalDateTime lastTime;

    /**
     * 登录时间
     */
    private LocalDateTime createdTime;

}
