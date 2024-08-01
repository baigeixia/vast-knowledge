package com.vk.comment.domain.vo;

import com.vk.user.domain.AuthorInfo;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 评论基类
 */
@Data
public class CommentListBase {
    private Long id;
    private Long AuthorId;
    private AuthorInfo Author;
    private String text;
    private String image;
    private LocalDateTime time;
    private Long likes;
}

