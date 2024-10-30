package com.vk.user.mapper;

import com.mybatisflex.core.BaseMapper;
import com.vk.user.domain.ApUser;
import com.vk.user.domain.ApUserInfo;
import com.vk.user.domain.AuthorInfo;
import com.vk.user.domain.vo.LocalUserInfoVo;
import com.vk.user.domain.vo.UserInfoVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Set;

/**
 * APP用户信息 映射层。
 *
 * @author 张三
 * @since 2024-05-13
 */
public interface ApUserMapper extends BaseMapper<ApUser> {


    List<AuthorInfo> selectListOrInfo(@Param("ids") Set<Long> ids);
    @Select("select name from ap_user where  id=#{id}")
    String getUserName(@Param("id") Long userId);
}
