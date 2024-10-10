package com.vk.user.mapper;

import com.mybatisflex.core.BaseMapper;
import com.vk.user.domain.ApUserFollow;
import com.vk.user.domain.vo.FollowListVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * APP用户关注信息 映射层。
 *
 * @author 张三
 * @since 2024-05-13
 */
public interface ApUserFollowMapper extends BaseMapper<ApUserFollow> {

    List<FollowListVo> getList(@Param("userId") Long userId, @Param("page")Long page, @Param("size")Long size, @Param("localID")Long localID);
}
