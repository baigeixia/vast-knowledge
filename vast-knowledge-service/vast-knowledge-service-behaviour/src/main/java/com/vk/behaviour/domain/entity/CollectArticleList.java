package com.vk.behaviour.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CollectArticleList {
    private Long articleId;
    private LocalDateTime createdTime;
}
