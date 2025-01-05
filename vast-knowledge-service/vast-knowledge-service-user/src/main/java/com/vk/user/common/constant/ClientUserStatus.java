package com.vk.user.common.constant;

import lombok.Getter;

/**
 * 用户状态
 * 
 * @author vk
 */
@Getter
public enum ClientUserStatus
{
    CLIENT_OK(false, "正常"), CLIENT_DISABLE(true, "停用");


    private final Boolean code;
    private final String info;

    ClientUserStatus(Boolean code, String info)
    {
        this.code = code;
        this.info = info;
    }

}
