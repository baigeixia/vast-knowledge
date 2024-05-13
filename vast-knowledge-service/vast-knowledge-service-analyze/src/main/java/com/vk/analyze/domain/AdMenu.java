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
 * 菜单资源信息 实体类。
 *
 * @author 张三
 * @since 2024-05-13
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "ad_menu")
public class AdMenu implements Serializable {

    @Id
    private Long id;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 菜单代码
     */
    private String code;

    /**
     * 父菜单
     */
    private Long parentId;

    /**
     * 登录时间
     */
    private LocalDateTime createdTime;

}
