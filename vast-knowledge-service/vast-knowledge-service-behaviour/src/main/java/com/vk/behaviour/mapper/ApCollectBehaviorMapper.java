package com.vk.behaviour.mapper;

import com.mybatisflex.core.BaseMapper;
import com.vk.behaviour.domain.ApCollectBehavior;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * APP收藏行为 映射层。
 *
 * @author 张三
 * @since 2024-09-03
 */
public interface ApCollectBehaviorMapper extends BaseMapper<ApCollectBehavior> {

    @Select("select article_id articleId ,repay_author_id repayAuthorId , created_time createdTime from ap_collect_behavior where author_id=#{userId} and operation=0  LIMIT #{page}, #{size} order by  created_time desc")
    List<ApCollectBehavior> getcollectBehaviorsList(Long userId, Long page, Long size);
}
