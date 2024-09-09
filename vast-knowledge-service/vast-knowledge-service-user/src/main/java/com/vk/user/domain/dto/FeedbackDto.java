package com.vk.user.domain.dto;

import lombok.Data;

@Data
public class FeedbackDto {
    /**
     * 反馈内容
     */
    private String content;

    /**
     * 反馈图片,多张逗号分隔
     */
    private String images;
}
