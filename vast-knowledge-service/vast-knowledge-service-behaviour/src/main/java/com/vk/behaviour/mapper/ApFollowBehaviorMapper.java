package com.vk.behaviour.mapper;

import com.mybatisflex.core.BaseMapper;
import com.vk.behaviour.domain.ApFollowBehavior;
import com.vk.behaviour.domain.entity.FollowBehaviorTimeCount;
import com.vk.behaviour.domain.entity.FollowNotifyCountMap;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * APP关注行为 映射层。
 *
 * @author 张三
 * @since 2024-05-13
 */
public interface ApFollowBehaviorMapper extends BaseMapper<ApFollowBehavior> {

    Long getFollowNotifyCount(@Param("userId") Long userId,@Param("page")Long page, @Param("size")Long size);

    List<FollowNotifyCountMap> getFollowNotifyCountMap(@Param("userId") Long userId, @Param("page")Long page, @Param("size")Long size);

    List<FollowBehaviorTimeCount> getFollowNotifyList(@Param("userId") Long userId,@Param("page")Long page, @Param("size")Long size);
}
