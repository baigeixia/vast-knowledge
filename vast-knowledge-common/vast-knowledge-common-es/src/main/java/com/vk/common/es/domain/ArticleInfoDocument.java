package com.vk.common.es.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @version 1.0
 * @description 说明
 * @package
 */
@Data
// es中的索引名称
@Document(indexName = "article")
public class ArticleInfoDocument implements Serializable {

    @Id
    private Long id;

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

    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second)
    private LocalDateTime createdTime;

    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second)
    private LocalDateTime publishTime;

    private Integer isDown;

    private Integer isDelete;
    
}
