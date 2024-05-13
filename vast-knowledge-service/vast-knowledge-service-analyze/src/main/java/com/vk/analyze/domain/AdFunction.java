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
 * 页面功能信息 实体类。
 *
 * @author 张三
 * @since 2024-05-13
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "ad_function")
public class AdFunction implements Serializable {

    @Id
    private Long id;

    /**
     * 功能名称
     */
    private String name;

    /**
     * 功能代码
     */
    private String code;

    /**
     * 父功能
     */
    private Long parentId;

    /**
     * 登录时间
     */
    private LocalDateTime createdTime;

}
