package com.vk.analyze.domain;

import com.mybatisflex.annotation.Column;
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
 * 文章数据统计 实体类。
 *
 * @author 张三
 * @since 2024-05-13
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "ad_article_statistics")
public class AdArticleStatistics implements Serializable {

    /**
     * 主键
     */
    @Id
    private BigInteger id;

    /**
     * 主账号ID
     */
    private BigInteger articleWeMedia;

    /**
     * 子账号ID
     */
    private BigInteger articleCrawlers;

    /**
     * 频道ID
     */
    private Long channelId;

    /**
     * 草读量
     */
    @Column(value = "read_20")
    private Long read20;

    /**
     * 读完量
     */
    @Column(value = "read_100")
    private Long read100;

    /**
     * 阅读量
     */
    private Long readCount;

    /**
     * 评论量
     */
    private Long comment;

    /**
     * 关注量
     */
    private Long follow;

    /**
     * 收藏量
     */
    private Long collection;

    /**
     * 转发量
     */
    private Long forward;

    /**
     * 点赞量
     */
    private Long likes;

    /**
     * 不喜欢
     */
    private Long unlikes;

    /**
     * unfollow
     */
    private Long unfollow;

    /**
     * 创建时间
     */
    private LocalDateTime createdTime;

}
