package com.vk.article.domain.vo;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ArticleListVo {

    private Long id;

    /**
     * 标题
     */
    private String title;


    private String images;

    /**
     * 简单描述
     */
    private String simpleDescription;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    private Integer status;

}
