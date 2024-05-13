package com.vk.admin.domain;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDateTime;

/**
 * APP用户文章列 实体类。
 *
 * @author 张三
 * @since 2024-05-13
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "ap_user_article_list")
public class ApUserArticleList implements Serializable {

    /**
     * 主键
     */
    @Id
    private BigInteger id;

    /**
     * 用户ID
     */
    private BigInteger userId;

    /**
     * 频道ID
     */
    private Long channelId;

    /**
     * 动态ID
     */
    private BigInteger articleId;

    /**
     * 是否展示
     */
    private Integer isShow;

    /**
     * 推荐时间
     */
    private LocalDateTime recommendTime;

    /**
     * 是否阅读
     */
    private Integer isRead;

    /**
     * 推荐算法
     */
    private Long strategyId;

}
