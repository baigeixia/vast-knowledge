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
 * APP分享行为 实体类。
 *
 * @author 张三
 * @since 2024-05-13
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "ap_share_behavior")
public class ApShareBehavior implements Serializable {

    @Id(keyType= KeyType.Generator, value= KeyGenerators.flexId)
    private Long id;

    /**
     * 实体ID
     */
    private Long entryId;

    /**
     * 文章ID
     */
    private Long articleId;

    /**
     * 0:微信 1:微信朋友圈 2:QQ 3:QQ空间 4:微博 
     */
    private Integer type;

    /**
     * 登录时间
     */
    private LocalDateTime createdTime;

}
