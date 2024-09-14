package com.vk.common.mq.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewCommentUpMsg {
    /**
     * 评论id
     */
    private Long commentId;
    /**
     * 点赞
     */
    private Integer num;
}
