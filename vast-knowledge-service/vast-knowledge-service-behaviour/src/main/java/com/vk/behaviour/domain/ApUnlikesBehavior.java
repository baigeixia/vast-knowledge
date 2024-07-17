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
 * APP不喜欢行为 实体类。
 *
 * @author 张三
 * @since 2024-05-13
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "ap_unlikes_behavior")
public class ApUnlikesBehavior implements Serializable {

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
     * 0:不喜欢 1:取消不喜欢
     */
    private Integer type;

    /**
     * 登录时间
     */
    private LocalDateTime createdTime;

}
