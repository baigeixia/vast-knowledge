package com.vk.comment.domain.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 评论返回结果级
 */
@Data
public class CommentListVo {
    private List<CommentList> comments =new ArrayList<>();
    private Long page;
    private Long size;
    private Long total;
}
