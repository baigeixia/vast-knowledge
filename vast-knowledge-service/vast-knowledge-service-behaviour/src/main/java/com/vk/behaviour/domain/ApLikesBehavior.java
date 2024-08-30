package com.vk.behaviour.domain;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.keygen.KeyGenerators;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import java.time.LocalDateTime;

/**
 * APP点赞行为 实体类。
 *
 * @author 张三
 * @since 2024-05-13
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "ap_likes_behavior")
public class ApLikesBehavior implements Serializable {

    @Id(keyType=KeyType.Generator, value= KeyGenerators.flexId)
    private Long id;

    /**
     * 点赞用户id
     */
    private Long authorId;

    /**
     * 评论id
     */
    private Long commentId;
    /**
     * 点赞用户名称
     */
    private String authorName;

    /**
     * 文章ID
     */
    private Long articleId;

    /**
     * 被点赞人
     */
    private Long repayAuthorId;

    /**
     * 点赞内容类型 0:文章 1:评论
     */
    private Integer type;

    /**
     * 0:点赞 1:取消点赞
     */
    private Integer operation;

    /**
     * 登录时间
     */
    private LocalDateTime createdTime;

}
