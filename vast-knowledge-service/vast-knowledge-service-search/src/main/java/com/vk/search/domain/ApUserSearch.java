package com.vk.search.domain;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDateTime;

/**
 * APP用户搜索信息 实体类。
 *
 * @author 张三
 * @since 2024-05-13
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "ap_user_search")
public class ApUserSearch implements Serializable {

    /**
     * 主键
     */
    @Id(keyType = KeyType.Auto)
    private BigInteger id;

    /**
     * 用户ID
     */
    private BigInteger entryId;

    /**
     * 搜索词
     */
    private String keyword;

    /**
     * 当前状态 0:无效 1:有效
     */
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime createdTime;

}
