package com.vk.behaviour.domain;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import java.time.LocalDateTime;

/**
 * APP关注行为 实体类。
 *
 * @author 张三
 * @since 2024-05-13
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "ap_follow_behavior")
public class ApFollowBehavior implements Serializable {

    @Id
    private Long id;

    /**
     * 实体ID
     */
    private Long authorId;

    /**
     * 文章ID
     */
    private Long articleId;

    /**
     * 关注用户ID
     */
    private Long followId;

    /**
     * 登录时间
     */
    private LocalDateTime createdTime;

}
