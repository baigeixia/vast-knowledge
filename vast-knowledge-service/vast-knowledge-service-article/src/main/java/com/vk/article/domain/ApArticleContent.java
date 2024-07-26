package com.vk.article.domain;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


/**
 * APP已发布文章内容 实体类。
 *
 * @author 张三
 * @since 2024-07-11
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "ap_article_content")
public class ApArticleContent implements Serializable {

    /**
     * 主键
     */
    @Id(keyType = KeyType.Auto)
    private Long id;

    /**
     * 文章id
     */
    private Long articleId;

    /**
     * 作者ID
     */
    private Long authorId;

    /**
     * 文章内容
     */
    private String content;

}
