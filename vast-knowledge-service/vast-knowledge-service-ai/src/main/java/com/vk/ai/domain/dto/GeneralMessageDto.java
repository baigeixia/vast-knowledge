package com.vk.ai.domain.dto;

import lombok.Data;

import java.util.List;

/**
 * 接收消息内容
 */
@Data
public class GeneralMessageDto {

    /**
     * 消息 详情 id
     */
    private String chatSessionId;

    /**
     * 消息 父级 id
     */
    private Integer parentMessageId;

    /**
     *  模型 id
     */
    private String modelId;

    /**
     * 消息内容
     */
    private String prompt;

    /**
     * 上传图片地址
     */
    private List<String> refFileIds;

    /**
     * 开启推理
     */
    private Boolean thinkingEnabled;
    /**
     * 开启搜索
     */
    private Boolean searchEnabled;
}
