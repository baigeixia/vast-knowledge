package com.vk.article.domain;

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
 * APP收藏信息 实体类。
 *
 * @author 张三
 * @since 2024-07-11
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "ap_collection")
public class ApCollection implements Serializable {

    @Id(keyType = KeyType.Auto)
    private Long id;

    /**
     * 实体ID
     */
    private Long entryId;

    /**
     * 文章ID
     */
    private Long articleId;

    /**
     * 点赞内容类型 0:文章 1:动态
     */
    private Integer type;

    /**
     * 创建时间
     */
    private LocalDateTime collectionTime;

    /**
     * 发布时间
     */
    private LocalDateTime publishedTime;

}
