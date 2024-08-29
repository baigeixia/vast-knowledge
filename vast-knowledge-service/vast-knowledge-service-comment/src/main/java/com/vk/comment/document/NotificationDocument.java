package com.vk.comment.document;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class NotificationDocument {

    /**
     * 主键
     */
    @Id
    private Long id;

    /**
     * 用户ID
     */
    private Long authorId;

    /**
     * 文章作者id
     */
    private Long arAuthorId;


    /**
     * 回复评论用户id
     */
    private  Long repayAuthorId;

    /**
     * 用户昵称
     */
    private String authorName;



    private Long entryId;

    /**
     * 频道ID
     */
    private Long channelId;

    /**
     * 评论内容类型 0:文章 1:动态
     */
    private Integer type;

    /**
     * 评论内容
     */
    @Field(type = FieldType.Text,analyzer = "ik_smart")
    private String content;

    private String image;

    /**
     * 点赞数
     */
    private Long likes;

    /**
     * 回复数
     */
    private Long reply;

    /**
     * 文章标记 0:普通评论 1:热点评论 2:推荐评论 3:置顶评论 4:精品评论 5:大V评论
     */
    private Integer flag;

    /**
     * 经度
     */
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
     * 评论排列序号
     */
    private Long ord;

    /**
     * 创建时间
     */
    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second)
    private LocalDateTime createdTime;

    /**
     * 更新时间
     */
    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second)
    private LocalDateTime  updatedTime;

    private Long commentId;

    private Long commentRepayId;

    private  Integer status;
}
