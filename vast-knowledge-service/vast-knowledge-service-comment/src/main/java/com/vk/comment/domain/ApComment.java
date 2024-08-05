package com.vk.comment.domain;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.keygen.KeyGenerators;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

import java.time.LocalDateTime;

/**
 * APP评论信息 实体类。
 *
 * @author 张三
 * @since 2024-05-13
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "ap_comment")
public class ApComment implements Serializable {

    /**
     * 主键
     */
    // @Id(keyType = KeyType.Auto)
    @Id(keyType=KeyType.Generator, value= KeyGenerators.flexId)
    private Long id;

    /**
     * 用户ID
     */
    private Long authorId;

    /**
     * 用户昵称
     */
    private String authorName;

    private Long entryId;

    /**
     * 频道ID
     */
    private Long channelId;

    /**
     * 评论内容类型 0:文章 1:动态
     */
    private Integer type;

    /**
     * 评论内容
     */
    private String content;

    private String image;

    /**
     * 点赞数
     */
    private Long likes;

    /**
     * 回复数
     */
    private Long reply;

    /**
     * 文章标记 0:普通评论 1:热点评论 2:推荐评论 3:置顶评论 4:精品评论 5:大V评论
     */
    private Integer flag;

    /**
     * 经度
     */
    private BigDecimal longitude;

    /**
     * 维度
     */
    private BigDecimal latitude;

    /**
     * 地理位置
     */
    private String address;

    /**
     * 评论排列序号
     */
    private Long ord;

    /**
     * 创建时间
     */
    private LocalDateTime createdTime;

    /**
     * 更新时间
     */
    private LocalDateTime updatedTime;

    @Column(isLogicDelete = true)
    private  Integer status;
}
