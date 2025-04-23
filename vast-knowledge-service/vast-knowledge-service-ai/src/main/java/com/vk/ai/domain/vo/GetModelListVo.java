package com.vk.ai.domain.vo;


import lombok.Data;

@Data
public class GetModelListVo {
    private String id;

    /**
     * 模型名称
     */
    private String modelName;

    /**
     * 模型id
     */
    private String modelId;


    /**
     * 模型归属
     */
    private String pertain;

    /**
     * 是否可思考
     */
    private Boolean isThink;

    /**
     * 是否可联网
     */
    private Boolean isSearch;
}
