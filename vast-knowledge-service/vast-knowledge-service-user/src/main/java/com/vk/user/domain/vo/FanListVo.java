package com.vk.user.domain.vo;

import lombok.Data;

@Data
public class FanListVo {
    /**
     * 粉丝ID
     */
    private Long id;

    /**
     * 粉丝昵称
     */
    private String username;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 个人介绍
     */
    private String introduction;

    /**
     * 是否关注
     */
    private Integer concerned;
}
