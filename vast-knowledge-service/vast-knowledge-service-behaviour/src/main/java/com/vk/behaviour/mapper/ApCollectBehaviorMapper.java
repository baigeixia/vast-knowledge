package com.vk.behaviour.mapper;

import com.mybatisflex.core.BaseMapper;
import com.vk.behaviour.domain.ApCollectBehavior;
import com.vk.behaviour.domain.entity.CollectArticleList;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
import org.apache.ibatis.type.LocalDateTimeTypeHandler;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * APP收藏行为 映射层。
 *
 * @author 张三
 * @since 2024-09-03
 */
public interface ApCollectBehaviorMapper extends BaseMapper<ApCollectBehavior> {

    @Select("select article_id articleId ,repay_author_id repayAuthorId , created_time createdTime from ap_collect_behavior where author_id=#{userId} and operation=0  ORDER BY  created_time DESC LIMIT #{page},#{size}")
    List<ApCollectBehavior> getcollectBehaviorsList(@Param("userId")Long userId, @Param("page") Long page, @Param("size")Long size);

    // @Select("select article_id `key` ,  created_time `value` from ap_collect_behavior where  author_id=#{userId} and operation=0 ORDER BY created_time DESC LIMIT #{page},#{size}")
    @Select("select article_id articleId, created_time createdTime from ap_collect_behavior where author_id=#{userId} and operation=0 ORDER BY created_time DESC LIMIT #{page},#{size}")
    Set<CollectArticleList> selectUserCollectIdS(@Param("userId")Long userId, @Param("page") Long page, @Param("size")Long size);


    @Select("SELECT operation  FROM ap_collect_behavior  WHERE author_id = #{userId} AND article_id =#{artId}   LIMIT 1")
    Integer getArticleCollectOne(@Param("userId") Long userId, @Param("artId")Long artId);
}
