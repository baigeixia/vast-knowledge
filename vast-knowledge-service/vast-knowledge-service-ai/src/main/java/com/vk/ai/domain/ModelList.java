package com.vk.ai.domain;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 *  实体类。
 *
 * @author 张三
 * @since 2025-03-31
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "model_list")
public class ModelList implements Serializable {

    @Id(keyType = KeyType.Auto)
    private Integer id;

    /**
     * 模型id
     */
    private String modelId;

    /**
     * model url
     */
    private String baseurl;

    /**
     * token 最大限制
     */
    private Integer tokenLimit;

    /**
     * 是否启用
     */
    private Long state;

    /**
     * 是否删除
     */
    private Long del;

}
