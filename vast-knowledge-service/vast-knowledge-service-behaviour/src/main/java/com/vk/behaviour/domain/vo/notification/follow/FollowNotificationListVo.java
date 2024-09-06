package com.vk.behaviour.domain.vo.notification.follow;

import com.vk.behaviour.domain.vo.notification.like.LikeNotificationInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FollowNotificationListVo {
    /**
     * 对应时间
     */
    private String statisticsTime;
    /**
     * 评论通知 详情
     */
    private List<FollowNotificationInfo> notificationInfoList;
}
