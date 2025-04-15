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
 * 对话详情 实体类。
 *
 * @author 张三
 * @since 2025-04-15
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "chat_message")
public class ChatMessage implements Serializable {

    @Id(keyType=KeyType.Generator, value= KeyGenerators.flexId)
    private String id;

    /**
     * 消息详情
     */
    private Integer infoId;

    /**
     * 本次 消息id 1是顶级
     */
    private Integer messageId;

    /**
     * 父级id
     */
    private Integer parentId;

    /**
     * 使用模型id
     */
    private String model;

    /**
     * 发送人
     */
    private String role;

    /**
     * 消息
     */
    private String content;

    /**
     * 上传图片
     */
    private String files;

    /**
     * 是否开启思考
     */
    private Boolean thinkingEnabled;

    /**
     * 思考内容
     */
    private String thinkingContent;

    /**
     * 思考已用秒数
     */
    private Integer thinkingElapsedSecs;

    /**
     * 回复状态
     */
    private String status;

    /**
     * 累计令牌使用情况
     */
    private String accumulatedTokenUsage;

    /**
     * 是否启用搜索
     */
    private Boolean searchEnabled;

    /**
     * 搜索状态
     */
    private String searchStatus;

    /**
     * 搜索结果
     */
    private String searchResults;

    /**
     * 使用token数
     */
    private Integer usingTokens;

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
