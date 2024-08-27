package com.vk.comment.document;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.core.keygen.KeyGenerators;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @version 1.0
 * @description 说明
 * @package com.itheima.search.document
 */
@Data
// es中的索引名称
@Document(indexName = "comment_repay")
public class ApCommentRepayDocument implements Serializable {

    /**
     * 主键
     */
    @Id
    @Field(type = FieldType.Long )
    private Long id;

    /**
     * 用户ID
     */
    private Long authorId;

    /**
     * 用户昵称
     */
    private String authorName;

    private Long commentId;

    private Long commentRepayId;

    /**
     * 评论内容
     */
    @Field(type = FieldType.Text,analyzer = "ik_smart")
    private String content;

    private String image;

    private Long likes;

    private BigDecimal longitude;

    /**
     * 维度
     */
    private BigDecimal latitude;

    /**
     * 地理位置
     */
    private String address;

    /**
     * 创建时间
     */
    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second)
    private LocalDateTime createdTime;

    /**
     * 更新时间
     */
    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second)
    private LocalDateTime updatedTime;

    private  Integer status;



}
