package com.vk.wemedia.domain;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


/**
 * 自媒体子账号权限信息 实体类。
 *
 * @author 张三
 * @since 2024-05-13
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "wm_user_auth")
public class WmUserAuth implements Serializable {

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
     * 资源类型 0:菜单 1:频道 2:按钮
     */
    private Integer type;

    /**
     * 资源名称
     */
    private String name;

}
