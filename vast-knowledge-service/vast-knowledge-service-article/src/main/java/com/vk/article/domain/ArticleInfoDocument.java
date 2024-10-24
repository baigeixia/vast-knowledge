package com.vk.article.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @version 1.0
 * @description 说明
 * @package com.itheima.search.document
 */
@Data
// es中的索引名称
@Document(indexName = "article")
public class ArticleInfoDocument implements Serializable {

    @Id
    private String id;

    @Field(type = FieldType.Text,analyzer = "ik_smart")
    private String title;

    private Long authorId;

    private String authorName;

    private Integer channelId;

    private String channelName;

    private Integer layout;

    private String images;

    private Integer likes;

    private Integer collection;

    private Integer comment;

    private Integer views;

    private LocalDateTime createdTime;

    private LocalDateTime publishTime;

    private Integer isDown;

    private Integer isDelete;
    
}
