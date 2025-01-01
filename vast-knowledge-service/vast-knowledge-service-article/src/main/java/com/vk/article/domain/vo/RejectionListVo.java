package com.vk.article.domain.vo;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RejectionListVo {

    private Long id;

    /**
     * 解决理由
     */
    private String rejection;
    /**
     * 违禁词提示 多词用，分割
     */
    private String prohibited;
    /**
     * 创造时间
     */
    private LocalDateTime createdTime;
}
