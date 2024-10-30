package com.vk.common.es.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;

@Data
@Document(indexName = "user_read")
public class UserReadDocument {
    /**
     * 阅读行为主键
     */
    @Id
    private  Long  id;
    /**
     * 用户id
     */
    private  Long  entryId;
    /**
     * 文章id
     */
    private  Long  articleId;
    /**
     * 文章标题
     */
    @Field(type = FieldType.Text,analyzer = "ik_smart")
    private  String  title;

    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second)
    private LocalDateTime updatedTime;
}
