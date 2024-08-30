package com.vk.behaviour.domain.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 添加喜欢 消息
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LikeMsgDto extends BaseMsgDto implements Serializable {
    /**
     * 文章id
     */
    private Long articleId;
    /**
     * 顶级父评论id
     */
    private Long commentId;
    /**
     * 子评论id
     */
    private Long commentReId;
}
