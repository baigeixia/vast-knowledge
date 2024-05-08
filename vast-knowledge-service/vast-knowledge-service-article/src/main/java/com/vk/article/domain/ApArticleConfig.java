package com.vk.article.domain;


import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @description <p>APP已发布文章配置 </p>
 *
 * @version 1.0
 * @package com.vk.article.pojo
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Table("ap_article_config")
public class ApArticleConfig implements Serializable {


    @Id(keyType = KeyType.Auto)
    private Long id;

    private Long articleId;

    private Integer isComment;

    private Integer isForward;

    private Integer isDown;

    private Integer isDelete;


}
