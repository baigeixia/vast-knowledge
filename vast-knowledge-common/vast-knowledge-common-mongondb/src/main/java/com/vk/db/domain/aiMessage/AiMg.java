package com.vk.db.domain.aiMessage;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document
public class AiMg {

    @Id
    private String id;

    /**
     * 消息详情
     */
    @Indexed
    private String infoId;

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
    @Indexed
    private String modelId;

    /**
     * 使用模型id
     */
    private String modelName;

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
