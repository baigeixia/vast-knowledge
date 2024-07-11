package com.vk.article.domain;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * APP设备码信息 实体类。
 *
 * @author 张三
 * @since 2024-07-11
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "ap_equipment_code")
public class ApEquipmentCode implements Serializable {

    @Id
    private BigInteger id;

    /**
     * 用户ID
     */
    private BigInteger equipmentId;

    /**
     * 设备码
     */
    private String code;

}
