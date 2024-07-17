package com.vk.user.domain;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import java.time.LocalDateTime;

/**
 * APP用户频道信息 实体类。
 *
 * @author 张三
 * @since 2024-05-13
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "ap_user_channel")
public class ApUserChannel implements Serializable {

    @Id
    private Long id;

    /**
     * 用户ID
     */
    private Long channelId;

    /**
     * 文章ID
     */
    private Long userId;

    private String name;

    /**
     * 频道排序
     */
    private Integer ord;

    /**
     * 登录时间
     */
    private LocalDateTime createdTime;

}
