package com.vk.wemedia.domain;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class WmMaterialFeign {
    /**
     * 主键
     */
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
     * 创建时间
     */
    private LocalDateTime createdTime;
}
