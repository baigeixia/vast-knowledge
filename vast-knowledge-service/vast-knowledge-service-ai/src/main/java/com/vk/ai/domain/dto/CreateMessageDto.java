package com.vk.ai.domain.dto;


import lombok.Data;

@Data
public class CreateMessageDto {

    /**
     * 消息 详情 id
     */
    private String characterId;

    /**
     *  模型 id
     */
    private String modelId;
}
