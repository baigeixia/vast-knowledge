package com.vk.behaviour.domain.entity;

import lombok.Data;

@Data
public class LikesBehaviorTimeCount {
    private Long articleId;
    private Long commentId;
    private Integer total;
    private String time;
}
