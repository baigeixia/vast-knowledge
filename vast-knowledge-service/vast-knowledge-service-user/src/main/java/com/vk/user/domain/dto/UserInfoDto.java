package com.vk.user.domain.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class UserInfoDto {
    /**
     * userid主键
     */
    private Long id;
    /**
     * 用户名
     */
    private String name;

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
     * 公司
     */
    private String company;

    /**
     * 职业
     */
    private String occupation;

    /**
     * 生日
     */
    private LocalDate birthday;

    /**
     * 个人格言
     */
    private String introduction;


}
