package com.vk.article.util;

import lombok.Getter;

/**
 * 文章 状态
 */
@Getter
public enum ArticleStatus {
    DRAFT(1), // 草稿
    UNDER_REVIEW(2), // 审核中
    MISS(3), // 审核失败
    PASS(8), // 审核通过
    PUBLISHED(9), // 已发布
    DOWN(1), // 下架
    NOT_DOWN(0); // 上架

    private final int status;

    ArticleStatus(int status) {
        this.status = status;
    }

}
