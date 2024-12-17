package com.vk.behaviour.mapper;

import com.mybatisflex.core.BaseMapper;
import com.vk.behaviour.domain.ApUnlikesBehavior;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * APP不喜欢行为 映射层。
 *
 * @author 张三
 * @since 2024-05-13
 */
public interface ApUnlikesBehaviorMapper extends BaseMapper<ApUnlikesBehavior> {

    @Select("select id from ap_unlikes_behavior where  article_id=#{id}")
    Long getOne(@Param(value = "id") Long id);
}
