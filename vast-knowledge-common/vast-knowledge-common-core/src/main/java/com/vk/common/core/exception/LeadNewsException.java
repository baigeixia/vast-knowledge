package com.vk.common.core.exception;


import com.vk.common.core.constant.HttpStatus;
import com.vk.common.core.enums.HttpCodeEnum;

/**
 * @version 1.0
 * @description 自定义异常，用于区分系统异常与用户友好提示
 * @package
 */

public class LeadNewsException extends RuntimeException{
    /**
     * 错误的状态码
     */
    private Integer code=500;

    /**
     * 错误提示信息
     */
    private final String message;

    public LeadNewsException(String message){
        super(message);
        this.message = message;
    }

    public LeadNewsException(Integer code, String message){
        super(message);
        this.code = code;
        this.message = message;
    }

    public LeadNewsException(HttpCodeEnum codeEnum){
        super(codeEnum.getMessage());
        this.code = codeEnum.getCode();
        this.message = codeEnum.getMessage();
    }

    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
