package com.vk.analyze.domain;

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
 * 标签信息 实体类。
 *
 * @author 张三
 * @since 2024-05-13
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "ad_label")
public class AdLabel implements Serializable {

    @Id(keyType = KeyType.Auto)
    private Long id;

    /**
     * 频道名称
     */
    private String name;

    /**
     * 0:系统增加 1:人工增加
     */
    private Integer type;

    /**
     * 创建时间
     */
    private LocalDateTime createdTime;

}
