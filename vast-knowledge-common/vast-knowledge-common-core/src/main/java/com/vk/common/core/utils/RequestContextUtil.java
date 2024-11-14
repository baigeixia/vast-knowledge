package com.vk.common.core.utils;


import com.vk.common.core.constant.SecurityConstants;
import com.vk.common.core.exception.LeadNewsException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;


public class RequestContextUtil {
    /**
     * 获取登录的用户的ID 可以是自媒体账号 也可以是 平台账号 也可以是app账号
     * @return
     */
    public static Long getUserId(){
        String header = RequestContextUtil.getHeader(SecurityConstants.DETAILS_USER_ID);

        if(StringUtils.isEmpty(header)){
            throw  new LeadNewsException(401,"请登录");
        }
        return Long.parseLong(header);
    }
    /**
     * 获取登录的用户的ID 可以是自媒体账号 也可以是 平台账号 也可以是app账号
     * @return
     */
    public static Long getUserIdNotLogin(){
        String header = RequestContextUtil.getHeader(SecurityConstants.DETAILS_USER_ID);
        return StringUtils.isEmpty(header) ? null: Long.parseLong(header);
    }

    /**
     * 是否为匿名登录
     * @return
     */
    public static String getUserName(){
        String headerValue = RequestContextUtil.getHeader(SecurityConstants.DETAILS_USERNAME);
        // 解码URL编码的字符串
        return URLDecoder.decode(headerValue, StandardCharsets.UTF_8);
    }

    public static String getHeader(String headerName){
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        //获取路由转发的头信息
        return request.getHeader(headerName);
    }
}