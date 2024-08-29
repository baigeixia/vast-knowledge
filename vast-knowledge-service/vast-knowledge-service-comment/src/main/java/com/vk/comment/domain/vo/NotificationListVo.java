package com.vk.comment.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class NotificationListVo {
    /**
     * 对应时间
     */
    private String statisticsTime;
    /**
     * 评论通知 详情
     */
    private List<NotificationInfo> notificationInfoList;
}
