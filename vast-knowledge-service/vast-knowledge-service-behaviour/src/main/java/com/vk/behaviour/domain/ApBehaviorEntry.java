package com.vk.behaviour.domain;

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
 * APP行为实体,一个行为实体可能是用户或者设备，或者其它 实体类。
 *
 * @author 张三
 * @since 2024-05-13
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "ap_behavior_entry")
public class ApBehaviorEntry implements Serializable {

    /**
     * 主键
     */
    @Id(keyType = KeyType.Auto)
    private BigInteger id;

    /**
     * 实体类型 0:终端设备 1:用户
     */
    private Integer type;

    /**
     * 实体ID
     */
    private BigInteger entryId;

    /**
     * 创建时间
     */
    private LocalDateTime createdTime;

}
