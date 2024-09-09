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
import java.math.BigInteger;
import java.time.LocalDateTime;

/**
 * APP用户举报信息 实体类。
 *
 * @author 张三
 * @since 2024-09-09
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "ap_user_report")
public class ApUserReport implements Serializable {

    /**
     * 主键
     */
    @Id(keyType= KeyType.Generator, value= KeyGenerators.flexId)
    private BigInteger id;

    /**
     * 举报人用户ID
     */
    private Long userId;

    /**
     * 举报人昵称
     */
    private String userName;

    /**
     * 被举报人用户ID
     */
    private Long reportUserId;

    /**
     * 被举报人昵称
     */
    private String reportUserName;

    /**
     * 文章id
     */
    private Long articleId;

    /**
     * 举报原因
     */
    private String reportReason;

    /**
     * 举报内容
     */
    private String reportContent;

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
