package com.vk.user.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MsgInfo {
    private Long id;
    private String text;
    private LocalDateTime createdTime;
    private String image;
    private String userType;
    private Boolean showTimestamp;
    private Boolean isCanceled;
}
