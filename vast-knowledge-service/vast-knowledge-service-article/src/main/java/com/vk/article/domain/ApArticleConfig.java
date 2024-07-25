package com.vk.article.domain;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


/**
 * APP已发布文章配置 实体类。
 *
 * @author 张三
 * @since 2024-07-11
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "ap_article_config")
public class ApArticleConfig implements Serializable {

    /**
     * 主键
     */
    @Id(keyType = KeyType.Auto)
    private Long id;

    /**
     * 文章ID
     */
    private Long articleId;

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
