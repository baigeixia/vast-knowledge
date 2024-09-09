package com.vk.user.mapper;

import com.mybatisflex.core.BaseMapper;
import com.vk.user.domain.ApUserLetter;
import com.vk.user.domain.vo.MsgUserListVo;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;

/**
 * APP用户私信信息 映射层。
 *
 * @author 张三
 * @since 2024-05-13
 */
public interface ApUserLetterMapper extends BaseMapper<ApUserLetter> {

    ArrayList<MsgUserListVo> getRecentList(@Param("userId") Long userId,@Param("page") Integer page,@Param("size") Integer size);

    ArrayList<MsgUserListVo> getStrangerList(@Param("userId") Long userId,@Param("page") Integer page,@Param("size") Integer size);

    ArrayList<MsgUserListVo> getMutualConcernList(@Param("userId") Long userId,@Param("page") Integer page,@Param("size") Integer size);
}
