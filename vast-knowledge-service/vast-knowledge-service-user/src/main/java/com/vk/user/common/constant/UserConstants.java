package com.vk.user.common.constant;

public class UserConstants {

    public static String USER_INFO_KEY="user_info_key";

    public static String redisUserInfoKey(Long id){
        return UserConstants.USER_INFO_KEY+":"+id;
    }
}
