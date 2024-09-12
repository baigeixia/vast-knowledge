package com.vk.user.domain.vo;

import com.mybatisflex.annotation.ColumnMask;
import com.mybatisflex.core.mask.Masks;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LocalUserInfoVo {
    /**
     * userid主键
     */
    private Long id;
    /**
     * 用户名
     */
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
    private LocalDateTime updatedTime;
}
