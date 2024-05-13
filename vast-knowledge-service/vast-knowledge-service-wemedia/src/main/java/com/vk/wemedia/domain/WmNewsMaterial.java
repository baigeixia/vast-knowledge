package com.vk.wemedia.domain;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * 自媒体图文引用素材信息 实体类。
 *
 * @author 张三
 * @since 2024-05-13
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "wm_news_material")
public class WmNewsMaterial implements Serializable {

    /**
     * 主键
     */
    @Id(keyType = KeyType.Auto)
    private BigInteger id;

    /**
     * 素材ID
     */
    private BigInteger materialId;

    /**
     * 图文ID
     */
    private BigInteger newsId;

    /**
     * 引用类型 0:内容引用 1:主图引用
     */
    private Integer type;

    /**
     * 引用排序
     */
    private Integer ord;

}
