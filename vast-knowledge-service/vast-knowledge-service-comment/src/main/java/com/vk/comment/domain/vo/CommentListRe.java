package com.vk.comment.domain.vo;

import com.vk.user.domain.AuthorInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 子评论
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CommentListRe extends CommentListBase{
    private Long  commentRepayId;
    private Long  commentId;
    private AuthorInfo reply;
}
