package com.vk.common.es.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
// es中的索引名称
@Document(indexName = "userinfo")
public class UserInfoDocument  implements Serializable {

    /**
     * userid主键
     */
    @Id
    private Long id;
    /**
     * 用户名
     */
    @Field(type = FieldType.Text,analyzer = "ik_max_word",searchAnalyzer="ik_smart")
    private String name;
    /**
     * 手机号
     */
    private String phone;

    /**
     * 头像
     */
    private String image;
    /**
     * 职位
     */
    private String position;
    /**
     * 0:男 1:女 2:未知
     */
    private Integer sex;
    /**
     * 注册时间
     */
    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second)
    private LocalDateTime createdTime;
    /**
     * 公司
     */
    private String company;

    /**
     * 职业
     */
    private String occupation;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 生日
     */
    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second)
    private LocalDateTime birthday;

    /**
     * 个人格言
     */
    private String introduction;

    /**
     * 归属地
     */
    private String location;
    /**
     * 粉丝数量
     */
    private Long fans;

    /**
     * 关注数量
     */
    private Long follows;

    /**
     * 谁可以给我发私信 1所有人 2我关注的人 3互相关注的人  9关闭私信
     */
    private Integer isSendMessage;

    /**
     * 是否允许推荐我给好友
     */
    private Integer isRecommendMe;


    /**
     * 是否允许给我推荐好友
     */
    private Integer isRecommendFriend;

    /**
     * 是否分享页面显示头像
     */
    private Integer isDisplayImage;

    /**
     * 更新时间
     */
    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second)
    private LocalDateTime updatedTime;
}
