package com.vk.analyze.domain;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import com.vk.common.core.web.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 频道信息 实体类。
 *
 * @author 张三
 * @since 2024-05-13
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "ad_channel")
public class AdChannel implements Serializable {

    @Id(keyType = KeyType.Auto)
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

}
