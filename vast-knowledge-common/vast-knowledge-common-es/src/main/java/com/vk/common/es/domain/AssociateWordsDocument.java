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
@Document(indexName = "associate_words")
public class AssociateWordsDocument implements Serializable {

    @Id
    private Long id;

    /**
     * 联想词
     */
    @Field(type = FieldType.Text,analyzer = "ik_max_word",searchAnalyzer="ik_smart")
    private String associateWords;

    /**
     * 创建时间
     */
    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second)
    private LocalDateTime createdTime;


}
