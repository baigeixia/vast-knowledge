package com.vk.admin.domain;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * APP用户动态列 实体类。
 *
 * @author 张三
 * @since 2024-05-13
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "ap_user_dynamic_list")
public class ApUserDynamicList implements Serializable {

    /**
     * 主键
     */
    @Id
    private BigInteger id;

    /**
     * 用户ID
     */
    private BigInteger userId;

    /**
     * 动态ID
     */
    private BigInteger dynamicId;

}
