package com.vk.article.mapper;

import com.mybatisflex.core.BaseMapper;
import com.vk.article.domain.ApArticle;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 已发布的文章信息 映射层。
 *
 * @author 张三
 * @since 2024-07-11
 */
public interface ApArticleMapper extends BaseMapper<ApArticle> {

    @Select("SELECT a.id from  ap_article a LEFT JOIN ap_article_config c ON a.id = c.article_id WHERE a.author_id = #{userId}  and c.is_down=0 and c.is_delete=0    ORDER BY  created_time DESC LIMIT #{page},5")
    List<Long> selectUserIdGetList(@Param("userId") Long userId, @Param("page")Long page);
}
