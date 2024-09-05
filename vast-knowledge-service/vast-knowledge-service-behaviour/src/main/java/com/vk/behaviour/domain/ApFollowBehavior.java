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

    @Id(keyType= KeyType.Generator, value= KeyGenerators.flexId)
    private Long id;

    /**
     * 用户id
     */
    private Long authorId;

    /**
     * 用户名称
     */
    private String authorName;

    /**
     * 关注用户ID
     */
    private Long followId;

    /**
     * 登录时间
     */
    private LocalDateTime createdTime;

}
