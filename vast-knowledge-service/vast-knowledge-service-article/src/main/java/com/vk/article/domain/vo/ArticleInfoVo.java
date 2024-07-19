package com.vk.article.domain.vo;

import com.vk.article.domain.ApArticle;
import lombok.Data;

/**
 * 已发布的文章信息 实体类。
 *
 * @author 张三
 * @since 2024-07-11
 */

@Data
public class ArticleInfoVo extends ApArticle {

    /**
     * 是否可评论
     */
    private Integer isComment;

    /**
     * 是否转发
     */
    private Integer isForward;

    /**
     * 是否下架
     */
    private Integer isDown;

    /**
     * 是否已删除
     */
    private Integer isDelete;

}
