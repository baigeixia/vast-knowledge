package com.vk.ai.domain;

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
 * 消息详情 实体类。
 *
 * @author 张三
 * @since 2025-04-15
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "chat_info")
public class ChatInfo implements Serializable {

    @Id(keyType=KeyType.Generator, value= KeyGenerators.flexId)
    private String id;

    /**
     * 标题
     */
    private String title;

    /**
     * 使用用户
     */
    private Long userId;

    /**
     * 序列号
     */
    private Integer usedToken;

    /**
     * 当前最大 消息 ID
     */
    private Integer currentMessageId;

    /**
     * 创造时间
     */
    private LocalDateTime creatingTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 是否删除
     */
    private Boolean del;

}
