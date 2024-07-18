package com.vk.db.domain.article;



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
@Document
public class ArticleMg implements Serializable {


    /**
     * 主键
     */
    @Id
    private Long id;

    /**
     * 文章ID
     */
    @Indexed
    private Long articleId;

    /**
     * 文章内容
     */
    private String content;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "ArticleMg{" +
                "id=" + id +
                ", articleId=" + articleId +
                ", content='" + content + '\'' +
                '}';
    }
}
