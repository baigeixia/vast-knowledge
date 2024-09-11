package com.vk.user.mapper;

import com.mybatisflex.core.BaseMapper;
import com.vk.user.domain.ApUserLetter;
import com.vk.user.domain.vo.MsgInfo;
import com.vk.user.domain.vo.MsgUserListVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.ArrayList;
import java.util.List;

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

    List<MsgInfo> getMsgList(@Param("userId")Long userId, @Param("localUserId")Long localUserId, @Param("page")Integer page, @Param("size")Integer size);

    @Update("update ap_user_letter set is_read=1,read_time= NOW() where sender_id=#{localUserId} and user_id=#{userId} ")
    void clearUnreadMsg(@Param("userId")Long userId, @Param("localUserId")Long localUserId);

    @Update("update ap_user_letter set status_pointing=#{localUserId} where id=#{msgId} and (sender_id=#{localUserId} or user_id=#{localUserId}) ")
    void deleteMsgId(@Param("localUserId")Long localUserId,@Param("msgId") Long msgId);

    @Select("select status_pointing  from  ap_user_letter where id=#{msgId} and (sender_id=#{localUserId} or user_id=#{localUserId}) ")
    Long selectUserIdAndMsgId(@Param("localUserId")Long localUserId,@Param("msgId") Long msgId);

    @Update("update ap_user_letter set status_pointing=110 where id=#{msgId} and (sender_id=#{localUserId} or user_id=#{localUserId}) ")
    void deleteMsgAll(@Param("localUserId")Long localUserId,@Param("msgId") Long msgId);

}
