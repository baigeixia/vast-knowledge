package com.vk.wemedia.domain;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


/**
 * 自媒体子账号信息 实体类。
 *
 * @author 张三
 * @since 2024-05-13
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "wm_sub_user")
public class WmSubUser implements Serializable {

    /**
     * 主键
     */
    @Id
    private Long id;

    /**
     * 主账号ID
     */
    private Long parentId;

    /**
     * 子账号ID
     */
    private Long childrenId;

}
