package com.vk.user.domain;

import com.mybatisflex.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import java.time.LocalDateTime;

/**
 * APP用户私信信息 实体类。
 *
 * @author 张三
 * @since 2024-05-13
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "ap_user_letter")
public class ApUserLetter implements Serializable {

    /**
     * 主键
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 发送人ID
     */
    private Long senderId;

    /**
     * 发送人昵称
     */
    private String senderName;

    /**
     * 私信内容
     */
    private String content;

    /**
     * 是否阅读
     */
    private Integer isRead;

    /**
     * 创建时间
     */
    private LocalDateTime createdTime;

    /**
     * 阅读时间
     */
    private LocalDateTime readTime;

}
