package com.vk.user.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysteamNotificationListVo {
    /**
     * 对应时间
     */
    private String statisticsTime;
    /**
     * 评论通知 详情
     */
    private List<SystemNotificationInfo> notificationInfoList;
}
