package com.vk.behaviour.mapper;

import com.mybatisflex.core.BaseMapper;
import com.vk.behaviour.domain.ApCollectBehavior;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
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

    @Select("select article_id  from ap_collect_behavior where  author_id=#{userId} and operation=0 ORDER BY created_time DESC LIMIT #{page},#{size}")
    Set<Long> selectUserCollectIdS(@Param("userId")Long userId, @Param("page") Long page, @Param("size")Long size);
}
