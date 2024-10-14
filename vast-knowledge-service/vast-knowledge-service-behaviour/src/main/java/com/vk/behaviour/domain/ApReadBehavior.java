package com.vk.behaviour.domain;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.keygen.KeyGenerators;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import java.time.LocalDateTime;

/**
 * APP阅读行为 实体类。
 *
 * @author 张三
 * @since 2024-05-13
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "ap_read_behavior")
public class ApReadBehavior implements Serializable {

    @Id(keyType= KeyType.Generator, value= KeyGenerators.flexId)
    private Long id;

    /**
     * 用户ID
     */
    private Long entryId;

    /**
     * 文章ID
     */
    private Long articleId;

    private Integer count;

    /**
     * 阅读时间单位秒
     */
    private Long readDuration;

    /**
     * 阅读百分比
     */
    private Integer percentage;
    private Integer maxPosition;

    /**
     * 文章加载时间
     */
    private Long loadDuration;

    /**
     * 登录时间
     */
    private LocalDateTime createdTime;

    private LocalDateTime updatedTime;

}
