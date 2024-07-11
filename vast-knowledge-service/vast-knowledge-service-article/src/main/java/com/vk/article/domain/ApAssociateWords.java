package com.vk.article.domain;

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
 * 联想词 实体类。
 *
 * @author 张三
 * @since 2024-07-11
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "ap_associate_words")
public class ApAssociateWords implements Serializable {

    @Id(keyType = KeyType.Auto)
    private Long id;

    /**
     * 联想词
     */
    private String associateWords;

    /**
     * 创建时间
     */
    private LocalDateTime createdTime;

}
