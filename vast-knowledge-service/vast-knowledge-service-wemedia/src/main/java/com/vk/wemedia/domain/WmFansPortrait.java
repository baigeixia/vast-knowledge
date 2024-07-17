package com.vk.wemedia.domain;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import java.time.LocalDateTime;

/**
 * 自媒体粉丝画像信息 实体类。
 *
 * @author 张三
 * @since 2024-05-13
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "wm_fans_portrait")
public class WmFansPortrait implements Serializable {

    /**
     * 主键
     */
    @Id
    private Long id;

    /**
     * 账号ID
     */
    private Long userId;

    /**
     * 画像项目
     */
    private String name;

    /**
     * 资源名称
     */
    private String value;

    private String burst;

    /**
     * 创建时间
     */
    private LocalDateTime createdTime;

}
