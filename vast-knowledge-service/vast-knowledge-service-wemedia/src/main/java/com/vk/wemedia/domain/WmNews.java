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
import java.time.LocalDateTime;

/**
 * 自媒体图文内容信息 实体类。
 *
 * @author 张三
 * @since 2024-05-13
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "wm_news")
public class WmNews implements Serializable {

    /**
     * 主键
     */
    @Id(keyType = KeyType.Auto)
    private BigInteger id;

    /**
     * 自媒体用户ID
     */
    private BigInteger userId;

    /**
     * 标题
     */
    private String title;

    /**
     * 图文内容
     */
    private String content;

    /**
     * 文章布局 0:无图文章 1:单图文章 3:多图文章
     */
    private Integer type;

    /**
     * 图文频道ID
     */
    private Long channelId;

    private String labels;

    /**
     * 创建时间
     */
    private LocalDateTime createdTime;

    /**
     * 提交时间
     */
    private LocalDateTime submitedTime;

    /**
     * 当前状态 0:草稿 1:提交（待审核） 2:审核失败 3:人工审核 4:人工审核通过 8:审核通过（待发布） 9:已发布
     */
    private Integer status;

    /**
     * 定时发布时间，不定时则为空
     */
    private LocalDateTime publishTime;

    /**
     * 拒绝理由
     */
    private String reason;

    /**
     * 发布库文章ID
     */
    private BigInteger articleId;

    /**
     * //图片用逗号分隔
     */
    private String images;

    private Integer enable;

}
