package com.vk.comment.domain.vo;

import lombok.Data;

import java.util.List;

@Data
public class CommentListVo {
    private List<CommentList> comments;
    private Long page;
    private Long size;
    private Long total;
}
