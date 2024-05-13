package com.vk.admin.domain;

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
 * APP用户粉丝信息 实体类。
 *
 * @author 张三
 * @since 2024-05-13
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "ap_user_fan")
public class ApUserFan implements Serializable {

    /**
     * 主键
     */
    @Id(keyType = KeyType.Auto)
    private BigInteger id;

    /**
     * 用户ID
     */
    private BigInteger userId;

    /**
     * 粉丝ID
     */
    private BigInteger fansId;

    /**
     * 粉丝昵称
     */
    private String fansName;

    /**
     * 粉丝忠实度 0:正常 1:潜力股 2:勇士 3:铁杆 4:老铁
     */
    private Integer level;

    /**
     * 创建时间
     */
    private LocalDateTime createdTime;

    /**
     * 是否可见我动态
     */
    private Integer isDisplay;

    /**
     * 是否屏蔽私信
     */
    private Integer isShieldLetter;

    /**
     * 是否屏蔽评论
     */
    private Integer isShieldComment;

}
