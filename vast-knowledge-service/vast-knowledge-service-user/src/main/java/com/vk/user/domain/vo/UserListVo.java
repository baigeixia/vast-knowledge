package com.vk.user.domain.vo;

import lombok.Data;

@Data
public class UserListVo {
    private Long id;
    /**
     * 用户名
     */
    private String email;
    /**
     * 0:正常 1:锁定
     */
    private Boolean status;
    /**
     * 头像
     */
    private String image;
}
