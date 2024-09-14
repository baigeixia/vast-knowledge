package com.vk.common.mq.domain;

import lombok.Data;

@Data

public class CommentVisitStreamMess {
    /**
     * 评论id
     */
    private Long commentId;
    /**
     * 修改数据的增量，可为正负
     */
    private Long like=0L;
}
