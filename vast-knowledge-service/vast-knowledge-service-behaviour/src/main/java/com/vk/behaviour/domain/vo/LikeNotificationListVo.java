package com.vk.behaviour.domain.vo;

import lombok.Data;

import java.util.List;

@Data
public class LikeNotificationListVo {
    /**
     * 对应时间
     */
    private String statisticsTime;
    /**
     * 评论通知 详情
     */
    private List<NotificationInfo> notificationInfoList;
}
