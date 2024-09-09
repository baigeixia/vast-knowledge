package com.vk.common.core.exception;


import com.vk.common.core.utils.StringUtils;
import org.springframework.util.ObjectUtils;

public class CustomSimpleThrowUtils {

    /**
     * 确定给定对象是否为空。
     * 此方法支持以下对象类型。
     * Optional：如果不是Optional.isPresent()，则视为空
     * Array：如果其长度为零，则视为空
     * CharSequence：如果其长度为零，则视为空
     * Collection：给Collection.isEmpty()
     * Map：给Map.isEmpty()
     * 如果给定对象非空且不是上述支持的类型之一，则此方法返回false。
     * @param o 对象
     * @param errorMsg 错误消息
     */
    public static void ObjectIsEmpty(Object o,String errorMsg){
        if (ObjectUtils.isEmpty(o)){
            throw new LeadNewsException(errorMsg);
        }
    }

    public static void LongIsEmpty(Long l,String errorMsg){
        if (StringUtils.isLongEmpty(l)){
            throw new LeadNewsException(errorMsg);
        }
    }

    public static void StringIsEmpty(String s,String errorMsg){
        if (StringUtils.isEmpty(s)){
            throw new LeadNewsException(errorMsg);
        }
    }
}
