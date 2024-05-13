package com.vk.behaviour.domain;

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

    @Id
    private BigInteger id;

    /**
     * 实体ID
     */
    private BigInteger entryId;

    /**
     * 文章ID
     */
    private BigInteger articleId;

    /**
     * 点赞内容类型 0:文章 1:动态
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
