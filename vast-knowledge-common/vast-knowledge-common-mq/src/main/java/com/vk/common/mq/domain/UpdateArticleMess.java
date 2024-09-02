package com.vk.common.mq.domain;

import lombok.Data;
import lombok.Getter;

/**
 * @version 1.0
 * @description 说明
 */
@Data
public class UpdateArticleMess {

    /**
     * 修改文章的字段类型
     */
    private UpdateArticleType type;
    /**
     * 文章ID
     */
    private Long articleId;
    /**
     * 修改数据的增量，可为正负
     */
    private Integer num;

    @Getter
    public enum UpdateArticleType{
        COLLECTION(1),COMMENT(2),LIKES(3),VIEWS(4);
        
        private int value=0;
        UpdateArticleType(int i) {
            this.value = i;
        }

    }
}
