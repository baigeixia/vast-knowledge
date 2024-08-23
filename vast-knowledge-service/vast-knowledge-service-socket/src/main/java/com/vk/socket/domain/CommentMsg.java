package com.vk.socket.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CommentMsg extends  BaseMsgDto{
    /**
     * 文章id
     */
    private Long articleId;
    /**
     * 评论id
     */
    private Long commentId;
}
