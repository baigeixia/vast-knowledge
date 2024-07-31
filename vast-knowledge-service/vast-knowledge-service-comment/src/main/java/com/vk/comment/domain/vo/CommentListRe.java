package com.vk.comment.domain.vo;

import com.vk.user.domain.AuthorInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class CommentListRe extends CommentListBase{
    private AuthorInfo reply;
}
