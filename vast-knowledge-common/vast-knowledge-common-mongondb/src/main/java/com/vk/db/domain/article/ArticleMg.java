package com.vk.db.domain.article;



import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

import java.time.LocalDateTime;

/**
 * 已发布的文章信息 实体类。
 *
 * @author 张三
 * @since 2024-07-11
 */
@Data
@Document
public class ArticleMg implements Serializable {


    /**
     * 主键
     */
    @Id
    private Long id;

    /**
     * 文章id
     */
    @Indexed
    private Long articleId;

    /**
     * 作者ID
     */
    @Indexed
    private Long authorId;

    /**
     * 文章内容
     */
    private String content;

    private String simpleDescription;


}
