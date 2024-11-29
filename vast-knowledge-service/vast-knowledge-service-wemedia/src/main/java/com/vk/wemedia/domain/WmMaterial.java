package com.vk.wemedia.domain;

import com.mybatisflex.annotation.Column;
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
 * 自媒体图文素材信息 实体类。
 *
 * @author 张三
 * @since 2024-05-13
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "wm_material")
public class WmMaterial implements Serializable {

    /**
     * 主键
     */
    @Id(keyType = KeyType.Auto)
    private Long id;

    /**
     * 自媒体用户ID
     */
    private Long userId;

    /**
     * 图片地址
     */
    private String url;

    /**
     * 素材类型 0:图片 1:视频
     */
    private Integer type;

    /**
     * 是否收藏
     */
    private Boolean isCollection;
    /**
     * 是否删除
     */
    @Column(isLogicDelete = true)
    private Boolean del;

    /**
     * 创建时间
     */
    private LocalDateTime createdTime;

}
