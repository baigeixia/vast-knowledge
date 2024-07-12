package com.vk.user.domain.dto;

import lombok.Data;

import java.util.Date;

/**
 * @version 1.0
 * @description 说明
 * @package com.vk.article.dto
 */
@Data
public class ArticleHomeDto {
    //页码
    private Long loaddir;
    // 最大时间
    private Date maxBehotTime;
    // 最小时间
    private Date minBehotTime;
    // 分页大小
    private Long size;
    // 频道ID
    private String tag;
    // size大小检测
    public void checkSize(){
        this.size = (null!=this.size&&this.size>=1&&this.size<=50)?this.size:10;
    }
}
