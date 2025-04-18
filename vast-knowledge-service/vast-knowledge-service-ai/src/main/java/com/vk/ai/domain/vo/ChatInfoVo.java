package com.vk.ai.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatInfoVo {
    private String id;

    /**
     * 标题
     */
    private String title;
    /**
     * 序列号
     */
    private String seqId;

    /**
     * 当前最大 消息 ID
     */
    private Integer currentMessageId;

    /**
     * 创造时间
     */
    private LocalDateTime creatingTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
