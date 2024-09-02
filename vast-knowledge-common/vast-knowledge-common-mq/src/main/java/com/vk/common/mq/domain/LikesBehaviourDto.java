package com.vk.common.mq.domain;

import lombok.Data;

/**
 * @version 1.0
 * @description 说明
 * @package com.itheima.behaviour.dto
 */
@Data
public class LikesBehaviourDto {
    // 设备ID
    private Long equipmentId;

    // 文章、动态、评论等ID
    private Long articleId;
    
    /**
     * 喜欢内容类型
     * 0文章
     * 1动态
     * 2评论
     */
    private Integer type;

    /**
     * 喜欢操作方式
     * 0 点赞
     * 1 取消点赞
     */
    private Integer operation;
}