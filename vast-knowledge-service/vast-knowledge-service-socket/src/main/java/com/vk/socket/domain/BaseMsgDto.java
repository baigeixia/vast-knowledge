package com.vk.socket.domain;

import lombok.Data;

/**
 * msg 基类
 */
@Data
public class BaseMsgDto {
    /**
     * 当前用户id
     */
    private Long userId;
}
