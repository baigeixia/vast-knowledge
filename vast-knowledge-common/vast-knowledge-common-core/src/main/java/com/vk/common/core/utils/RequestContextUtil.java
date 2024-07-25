package com.vk.common.core.utils;


import com.vk.common.core.constant.SecurityConstants;
import com.vk.common.core.exception.LeadNewsException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


public class RequestContextUtil {
    /**
     * 获取登录的用户的ID 可以是自媒体账号 也可以是 平台账号 也可以是app账号
     * @return
     */
    public static Long getUserId(){
        Long userid = Long.parseLong(RequestContextUtil.getHeader(SecurityConstants.DETAILS_USER_ID));
        if(StringUtils.isLongEmpty(userid)){
            throw  new LeadNewsException(401,"请登录");
        }
        return userid;
    }

    /**
     * 是否为匿名登录
     * @return
     */
    public static String getUserName(){
        return String.valueOf(RequestContextUtil.getHeader(SecurityConstants.DETAILS_USERNAME));
    }

    public static String getHeader(String headerName){
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        //获取路由转发的头信息
        return request.getHeader(headerName);
    }
}