package com.vk.common.core.domain;

import com.vk.common.core.constant.Constants;
import com.vk.common.core.exception.LeadNewsException;

public class ValidationUtils {

    /**
     * 验证 R 对象是否有效
     * 
     * @param result R<T> 对象
     * @param <T>    泛型类型
     * @throws LeadNewsException 如果验证失败
     */
    public static <T> void validateR(R<T> result,String errMsg) {
        if (result == null || result.getData() == null) {
            throw new LeadNewsException(errMsg);
        }
    }
    public static <T> boolean validateRSuccess(R<T> result) {
        return result != null && result.getCode() == Constants.SUCCESS &&  result.getData()!= null;
    }
}
