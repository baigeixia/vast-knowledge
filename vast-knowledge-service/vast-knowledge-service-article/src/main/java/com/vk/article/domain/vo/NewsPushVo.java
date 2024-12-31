package com.vk.article.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NewsPushVo {

    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 文章所属频道ID
     */
    private Long channelId;

    /**
     * 频道名称
     */
    private String channelName;

    /**
     * 文章图片
     */
    private String images;

    /**
     * 文章标签最多3个,逗号分隔
     */
    private String labels;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    private Integer status;

    private Boolean isDown;

    private Boolean isDelete;
}
