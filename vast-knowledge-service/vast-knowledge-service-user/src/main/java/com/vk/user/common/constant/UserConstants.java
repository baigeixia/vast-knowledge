package com.vk.user.common.constant;

public class UserConstants {

    public static String USER_INFO_KEY="USER_INFO_KEY";

    public static String redisUserInfoKey(Long id){
        return UserConstants.USER_INFO_KEY+":"+id;
    }
}
