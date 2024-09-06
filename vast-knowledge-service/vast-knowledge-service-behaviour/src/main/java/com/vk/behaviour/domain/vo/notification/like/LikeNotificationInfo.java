package com.vk.behaviour.domain.vo.notification.like;

import lombok.Data;

import java.util.List;

@Data
public class LikeNotificationInfo {
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
    private  String commentEndTime;

    /**
     * 对应用户
     */
    private List<LikeActors> actors;

    /**
     * 文章信息
     */
    private AttachInfo attachInfo;
}
