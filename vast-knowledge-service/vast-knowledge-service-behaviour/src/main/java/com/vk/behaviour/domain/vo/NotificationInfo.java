package com.vk.behaviour.domain.vo;

import lombok.Data;

@Data
public class NotificationInfo {
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
    private Actors actors;

    /**
     * 文章信息
     */
    private AttachInfo attachInfo;
}
