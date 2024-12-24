package com.vk.article.mapper;

import com.mybatisflex.core.BaseMapper;
import com.vk.article.domain.ApArticleConfig;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * APP已发布文章配置 映射层。
 *
 * @author 张三
 * @since 2024-07-11
 */
public interface ApArticleConfigMapper extends BaseMapper<ApArticleConfig> {

    @Update("update ap_article_config SET is_delete = 1  WHERE article_id = #{articleId} AND is_delete = 0;")
    void deleteByOne(@Param("articleId") Long articleId);


}
