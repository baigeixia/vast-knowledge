package com.vk.behaviour.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FollowBehaviorTimeCount {
    private Long articleId;
    private String authorName;
    private LocalDateTime followEndTime;
    private String dateTime;
}
