package com.vk.wemedia.domain;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import java.sql.Date;

/**
 * 自媒体图文数据统计 实体类。
 *
 * @author 张三
 * @since 2024-05-13
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "wm_news_statistics")
public class WmNewsStatistics implements Serializable {

    /**
     * 主键
     */
    @Id(keyType = KeyType.Auto)
    private Long id;

    /**
     * 主账号ID
     */
    private Long userId;

    /**
     * 子账号ID
     */
    private Long article;

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
     * 取消关注量
     */
    private Long unfollow;

    private String burst;

    /**
     * 创建时间
     */
    private Date createdTime;

}
