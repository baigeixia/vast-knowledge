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
import java.sql.Date;

/**
 * 自媒体粉丝数据统计 实体类。
 *
 * @author 张三
 * @since 2024-05-13
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "wm_fans_statistics")
public class WmFansStatistics implements Serializable {

    /**
     * 主键
     */
    @Id(keyType = KeyType.Auto)
    private BigInteger id;

    /**
     * 主账号ID
     */
    private BigInteger userId;

    /**
     * 子账号ID
     */
    private BigInteger article;

    private Long readCount;

    private Long comment;

    private Long follow;

    private Long collection;

    private Long forward;

    private Long likes;

    private Long unlikes;

    private Long unfollow;

    private String burst;

    /**
     * 创建时间
     */
    private Date createdTime;

}
