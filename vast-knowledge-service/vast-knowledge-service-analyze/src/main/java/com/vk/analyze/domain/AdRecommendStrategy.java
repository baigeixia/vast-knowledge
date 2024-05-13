package com.vk.analyze.domain;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 推荐策略信息 实体类。
 *
 * @author 张三
 * @since 2024-05-13
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "ad_recommend_strategy")
public class AdRecommendStrategy implements Serializable {

    /**
     * 主键
     */
    @Id
    private Long id;

    /**
     * 策略名称
     */
    private String name;

    /**
     * 策略描述
     */
    private String description;

    /**
     * 是否有效
     */
    private Boolean isEnable;

    /**
     * 分组ID
     */
    private Long groupId;

    /**
     * 创建时间
     */
    private LocalDateTime createdTime;

}
