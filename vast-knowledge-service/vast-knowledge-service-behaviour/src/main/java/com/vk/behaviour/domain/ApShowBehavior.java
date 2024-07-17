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
 * APP文章展现行为 实体类。
 *
 * @author 张三
 * @since 2024-05-13
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "ap_show_behavior")
public class ApShowBehavior implements Serializable {

    @Id
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
     * 是否点击
     */
    private Integer isClick;

    /**
     * 文章加载时间
     */
    private LocalDateTime showTime;

    /**
     * 登录时间
     */
    private LocalDateTime createdTime;

}
