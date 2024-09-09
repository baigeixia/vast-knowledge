package com.vk.user.domain.dto;

import lombok.Data;

@Data
public class ReportDto {


    /**
     * 被举报人用户ID
     */
    private Long reportUserId;

    /**
     * 被举报人昵称
     */
    private String reportUserName;

    /**
     * 文章id
     */
    private Long articleId;

    /**
     * 举报原因
     */
    private String reportReason;

    /**
     * 举报内容
     */
    private String reportContent;
}
