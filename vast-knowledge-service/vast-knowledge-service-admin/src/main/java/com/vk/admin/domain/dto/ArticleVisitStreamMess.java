package com.vk.admin.domain.dto;

import lombok.Data;

/**
 * @version 1.0
 * @description 说明
 * @package com.itheima.article.dto
 */
@Data
public class ArticleVisitStreamMess {
    /**
     * 文章id
     */
    private Long articleId;
    /**
     * 阅读
     */
    private Long view=0L;
    /**
     * 收藏
     */
    private Long collect=0L;
    /**
     * 评论
     */
    private Long comment=0L;
    /**
     * 点赞
     */
    private Long like=0L;
}
