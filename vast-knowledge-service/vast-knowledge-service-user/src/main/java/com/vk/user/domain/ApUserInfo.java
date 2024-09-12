package com.vk.user.domain;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.keygen.KeyGenerators;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * APP用户详情信息 实体类。
 *
 * @author 张三
 * @since 2024-05-13
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "ap_user_info")
public class ApUserInfo implements Serializable {

    /**
     * 主键
     */
    @Id(keyType= KeyType.Generator, value= KeyGenerators.flexId)
    private Long id;

    private Long userId;

    /**
     * 真是姓名
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
