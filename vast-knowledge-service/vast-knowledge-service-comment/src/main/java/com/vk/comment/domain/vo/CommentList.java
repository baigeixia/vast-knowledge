package com.vk.comment.domain.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * 父级评论
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CommentList extends CommentListBase {
    private Long childCommentCount;
    private List<CommentListRe> childComments =new ArrayList<>();
}
