package com.vk.common.core.utils;



public class AdminCheck {
    public static Boolean isAdmin(Long userId){
     return    userId != null && 1L == userId;
    }


}
