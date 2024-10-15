package com.vk.behaviour.mapper;

import com.mybatisflex.core.BaseMapper;
import com.vk.behaviour.domain.ApReadBehavior;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Set;

/**
 * APP阅读行为 映射层。
 *
 * @author 张三
 * @since 2024-05-13
 */
public interface ApReadBehaviorMapper extends BaseMapper<ApReadBehavior> {


    @Select("SELECT article_id as articleId , DATE(updated_time) as updatedTime  from ap_read_behavior WHERE entry_id =#{userId} ORDER BY updated_time DESC LIMIT #{page},#{size} ")
    List<ApReadBehavior> selectListUserRead(@Param("userId") Long userId, @Param("page")Long page, @Param("size")Long size);
}
