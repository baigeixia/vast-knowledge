package com.vk.article.mapper;

import com.mybatisflex.core.BaseMapper;
import com.vk.article.domain.ApArticle;
import com.vk.article.domain.ApArticleConfig;
import com.vk.article.domain.vo.ArticleData;
import com.vk.article.domain.vo.ArticleDataVo;
import com.vk.common.es.domain.ArticleInfoDocument;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * 已发布的文章信息 映射层。
 *
 * @author 张三
 * @since 2024-07-11
 */
public interface ApArticleMapper extends BaseMapper<ApArticle> {

    @Select("SELECT a.id from  ap_article a LEFT JOIN ap_article_config c ON a.id = c.article_id WHERE a.author_id = #{userId}  and c.is_down=0 and c.is_delete=0 and a.status=9   ORDER BY  created_time DESC LIMIT #{page},5")
    List<Long> selectUserIdGetList(@Param("userId") Long userId, @Param("page")Long page);

    @Select(value="select count(1) from ap_article apa inner join ap_article_config apc on apa.id=apc.article_id where publish_time<=#{publishTime}")
    Long selectCount(LocalDateTime now);

    List<ArticleInfoDocument> selectByPage(@Param(value = "start") Long start, @Param(value="size") Long size, @Param(value="publishTime") LocalDateTime publishTime);

    List<ArticleInfoDocument> selectForCondition(@Param(value="redisTime") LocalDateTime publishTime,@Param(value="nowTime") LocalDateTime nowTime);

    @Select("select count(1) from ap_article a inner join ap_article_config c ON a.id = c.article_id where a.author_id=#{localUserId} and a.id=#{articleId} and c.is_delete=0 ")
    Long selectCountOne(@Param(value = "articleId")Long articleId,@Param(value = "localUserId")Long localUserId);

    ArticleDataVo getArticleData(@Param(value = "userId")Long userId,@Param(value="startTime") LocalDateTime startTime,@Param(value="endTime") LocalDateTime endTime);


    // List<ArticleData> getArticleInfoData(@Param(value = "userId")Long userId, @Param(value="page")Long page,  @Param(value="size")Long size,@Param(value="startTime") LocalDateTime startTime,@Param(value="endTime") LocalDateTime endTime);
    List<ArticleData> getArticleInfoData(@Param(value = "userId")Long userId, @Param(value="page")Long page,  @Param(value="size")Long size);

    @Select("select count(*) from ap_article where author_id = #{userId} and status = 9  ")
    Long getArticleDataTotal(@Param(value = "userId")Long userId);


    @Select("SELECT `id`, `article_id`, `is_comment`, `is_forward`, `is_down`, `is_delete` FROM `ap_article_config` where article_id =#{articleId} LIMIT 1")
    ApArticleConfig selectOne(@Param(value = "articleId")Long articleId);

    @Select("SELECT a.`id`, a.`status` FROM `ap_article` a inner join ap_article_config c on a.id=c.article_id where a.author_id=#{userId} and  a.id = #{articleId} and c.is_delete=0 ")
    ApArticle getLocalArticle(@Param(value = "userId")Long userId, @Param(value = "articleId")Long articleId);

    @Transactional
    default void upArticleStatus(Long articleId, LocalDateTime updateTime, Integer status) {
        updateArticleStatus(articleId, updateTime, status);  // 更新文章状态
        updateArticleConfigStatus(articleId);                // 更新文章配置
    }

    @Update("UPDATE ap_article SET status = #{status}, update_time = #{updateTime} WHERE id = #{articleId}")
    void updateArticleStatus(@Param("articleId") Long articleId,
                             @Param("updateTime") LocalDateTime updateTime,
                             @Param("status") Integer status);

    @Update("UPDATE ap_article_config SET is_down = true WHERE article_id = #{articleId}")
    void updateArticleConfigStatus(@Param("articleId") Long articleId);

    @Select("select  COUNT(*) from ap_article where  status = 2")
    Long getAuditCount();

    @Select("select  id from ap_article where  status = 2 LIMIT #{startIndex},#{endIndex} ")
    List<Long> getAuditArticleId(@Param("startIndex")long startIndex, @Param("endIndex")long endIndex);

    void upStatus(@Param("articleIds") Set<Long> approvedIds, @Param("status") int i);

    void pushArticle(@Param("articleIds")Set<Long> approvedIds,@Param("status") int i,@Param("pushTime")LocalDateTime pushTime);

    @Select("select count(*) from ap_article where  author_id = #{userId} and id=#{id}  ")
    Long userArticle(@Param("userId")Long userId, @Param("id")Long id);


    List<ApArticle> listByIds(@Param("ids") Set<Long> ids);
}
