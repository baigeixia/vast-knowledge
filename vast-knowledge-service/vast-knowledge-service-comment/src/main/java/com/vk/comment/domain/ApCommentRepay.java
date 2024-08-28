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
 * APP评论回复信息 实体类。
 *
 * @author 张三
 * @since 2024-05-13
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "ap_comment_repay")
public class ApCommentRepay implements Serializable {

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
    /**
     * 回复评论用户id
     */
    private  Long repayAuthorId;

    private Long commentId;

    private Long commentRepayId;

    /**
     * 评论内容
     */
    private String content;

    private String image;

    private Long likes;

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
