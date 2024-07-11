package com.vk.article.domain;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDateTime;

/**
 * 热点文章 实体类。
 *
 * @author 张三
 * @since 2024-07-11
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "ap_hot_articles")
public class ApHotArticles implements Serializable {

    @Id(keyType = KeyType.Auto)
    private BigInteger id;

    private BigInteger entryId;

    /**
     * 频道ID
     */
    private Long tagId;

    /**
     * 频道名称
     */
    private String tagName;

    /**
     * 热度评分
     */
    private Long score;

    /**
     * 文章ID
     */
    private BigInteger articleId;

    /**
     * 省市
     */
    private Long provinceId;

    /**
     * 市区
     */
    private Long cityId;

    /**
     * 区县
     */
    private Long countyId;

    /**
     * 是否阅读
     */
    private Integer isRead;

    private LocalDateTime releaseDate;

    /**
     * 创建时间
     */
    private LocalDateTime createdTime;

}
