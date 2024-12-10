package com.vk.article.domain;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Table(value = "ap_rejection")
public class ApRejection {

    @Id(keyType = KeyType.Auto)
    private Long id;
    /**
     * 文章id
     */
    private Long articleId;
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
