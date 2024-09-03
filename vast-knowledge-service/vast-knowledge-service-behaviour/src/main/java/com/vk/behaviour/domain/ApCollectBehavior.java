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
 * APP收藏行为 实体类。
 *
 * @author 张三
 * @since 2024-09-03
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "ap_collect_behavior")
public class ApCollectBehavior implements Serializable {

    @Id
    private BigInteger id;

    /**
     * 用户id
     */
    private BigInteger authorId;

    /**
     * 用户名称
     */
    private String authorName;

    /**
     * 收藏文章ID
     */
    private BigInteger articleId;

    /**
     * 被收藏人
     */
    private Long repayAuthorId;

    /**
     * 0:收藏 1:取消收藏
     */
    private Integer operation;

    /**
     * 创造时间
     */
    private LocalDateTime createdTime;

}
