package com.vk.article.domain.vo;

import lombok.Data;

@Data
public class ArticleData {
    private Long id;
    private String title;
    private Long views;
    private Long comment;
    private Long collect;
    private Long likes;
}
