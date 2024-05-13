package com.vk.search.domain;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 搜索热词 实体类。
 *
 * @author 张三
 * @since 2024-05-13
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "ap_hot_words")
public class ApHotWords implements Serializable {

    @Id(keyType = KeyType.Auto)
    private Long id;

    /**
     * 热词
     */
    private String hotWords;

    /**
     * 0:热,1:荐,2:新,3:火,4:精,5:亮
     */
    private Integer type;

    /**
     * 热词日期
     */
    private String hotDate;

    /**
     * 创建时间
     */
    private LocalDateTime createdTime;

}
