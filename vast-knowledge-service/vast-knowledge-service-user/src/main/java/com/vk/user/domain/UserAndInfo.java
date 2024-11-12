package com.vk.user.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserAndInfo extends ApUser{

    /**
     * 名称
     */
    private String name;

    /**
     * 身份证号,aes加密
     */
    private String idno;

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
     * 性别
     */
    private Integer sex;
    /**
     * 职位
     */
    private String position;
    /**
     * 生日
     */
    private LocalDate birthday;

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
    private LocalDateTime updatedTime;
}
