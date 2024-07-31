package com.vk.comment.domain.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class CommentList extends CommentListBase {
    private String childCommentCount;
    private List<CommentListRe> childComments;
}
