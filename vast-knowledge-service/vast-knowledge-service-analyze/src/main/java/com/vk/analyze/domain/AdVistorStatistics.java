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
 * 访问数据统计 实体类。
 *
 * @author 张三
 * @since 2024-05-13
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "ad_vistor_statistics")
public class AdVistorStatistics implements Serializable {

    /**
     * 主键
     */
    @Id
    private Long id;

    /**
     * 日活
     */
    private Long activity;

    /**
     * 访问量
     */
    private Long vistor;

    /**
     * IP量
     */
    private Long ip;

    /**
     * 注册量
     */
    private Long register;

    /**
     * 创建时间
     */
    private LocalDateTime createdTime;

}
