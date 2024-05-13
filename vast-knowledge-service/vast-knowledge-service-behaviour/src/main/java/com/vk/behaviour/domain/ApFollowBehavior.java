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
     * 关注用户ID
     */
    private BigInteger followId;

    /**
     * 登录时间
     */
    private LocalDateTime createdTime;

}
