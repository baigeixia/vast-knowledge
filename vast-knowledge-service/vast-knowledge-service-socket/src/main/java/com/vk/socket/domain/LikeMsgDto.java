package com.vk.socket.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 添加喜欢 消息
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LikeMsgDto extends  BaseMsgDto{
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
