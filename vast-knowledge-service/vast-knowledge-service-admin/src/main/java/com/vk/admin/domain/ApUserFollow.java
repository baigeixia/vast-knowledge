package com.vk.admin.domain;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDateTime;

/**
 * APP用户关注信息 实体类。
 *
 * @author 张三
 * @since 2024-05-13
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "ap_user_follow")
public class ApUserFollow implements Serializable {

    /**
     * 主键
     */
    @Id(keyType = KeyType.Auto)
    private BigInteger id;

    /**
     * 用户ID
     */
    private BigInteger userId;

    /**
     * 关注作者ID
     */
    private BigInteger followId;

    /**
     * 粉丝昵称
     */
    private String followName;

    /**
     * 关注度 0:偶尔感兴趣 1:一般 2:经常 3:高度
     */
    private Integer level;

    /**
     * 是否动态通知
     */
    private Integer isNotice;

    /**
     * 创建时间
     */
    private LocalDateTime createdTime;

}
