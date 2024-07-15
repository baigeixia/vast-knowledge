package com.vk.common.core.enums;

/**
 * 用户状态
 * 
 * @author vk
 */
public enum ClientUserStatus
{
    CLIENT_OK(0, "正常"), CLIENT_DISABLE(1, "停用"), CLIENT_DELETED(1, "删除");


    private final Integer code;
    private final String info;

    ClientUserStatus(Integer code, String info)
    {
        this.code = code;
        this.info = info;
    }

    public Integer getCode()
    {
        return code;
    }

    public String getInfo()
    {
        return info;
    }
}
