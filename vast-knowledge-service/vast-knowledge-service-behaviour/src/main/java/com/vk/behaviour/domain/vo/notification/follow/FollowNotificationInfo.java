package com.vk.behaviour.domain.vo.notification.follow;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FollowNotificationInfo {
    /**
     * 提示消息
     */
    private  String verb;

    /**
     * 剩余未展示人数
     */
    private  Integer mergeCount;

    /**
     * 统计结束时间
     */
    private String followEndTime;

    /**
     * 对应用户
     */
    private List<FollowActors> actors;

}
