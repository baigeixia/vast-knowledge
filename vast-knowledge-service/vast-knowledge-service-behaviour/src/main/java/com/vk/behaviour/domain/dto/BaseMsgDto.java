package com.vk.behaviour.domain.dto;

import lombok.Data;

/**
 * msg 基类
 */
@Data
public class BaseMsgDto {
    /**
     * 当前用户id
     */
    private Long authorId;

    /**
     * 当前用户id
     */
    private Long commentId;
}
