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

    /**
     * 是否可评论
     */
    private Boolean isComment;

    /**
     * 是否转发
     */
    private Boolean isForward;

    /**
     * 是否下架
     */
    private Boolean isDown;

    /**
     * 是否已删除
     */
    private Boolean isDelete;

}
