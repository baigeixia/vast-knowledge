package com.vk.user.domain;

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
 * APP用户反馈信息 实体类。
 *
 * @author 张三
 * @since 2024-05-13
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "ap_user_feedback")
public class ApUserFeedback implements Serializable {

    /**
     * 主键
     */
    @Id(keyType= KeyType.Generator, value= KeyGenerators.flexId)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 发送人昵称
     */
    private String userName;

    /**
     * 内容
     */
    private String content;

    /**
     * 反馈图片,多张逗号分隔
     */
    private String images;

    /**
     * 是否阅读
     */
    private Integer isSolve;

    private String solveNote;

    /**
     * 阅读时间
     */
    private LocalDateTime solvedTime;

    /**
     * 创建时间
     */
    private LocalDateTime createdTime;

}
