package com.vk.user.mapper;

import com.mybatisflex.core.BaseMapper;
import com.vk.common.es.domain.UserInfoDocument;
import com.vk.user.domain.ApUserInfo;
import com.vk.user.domain.vo.UserInfoVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

/**
 * APP用户详情信息 映射层。
 *
 * @author 张三
 * @since 2024-05-13
 */
public interface ApUserInfoMapper extends BaseMapper<ApUserInfo> {

    @Select("SELECT i.id as id , i.is_send_message as isSendMessage ,i.is_recommend_me as isRecommendMe  ,i.is_recommend_friend as isRecommendFriend,i.is_display_image as isDisplayImage from  ap_user a LEFT JOIN  ap_user_info i on a.id=i.user_id WHERE a.status =0 AND a.id=#{userid}")
    ApUserInfo selectOneByUserId(@Param("userid") Long userid);

    UserInfoVo selectGetInfo(@Param("userId")Long id);

    @Select(value="SELECT count(1) from  ap_user u INNER JOIN ap_user_info i on u.id=i.user_id WHERE u.status=0 and u.created_time <= #{now}")
    Long selectCount(LocalDateTime now);

    List<UserInfoDocument> selectByPage(@Param(value = "start") Long start, @Param(value="size") Long size, @Param(value="now") LocalDateTime now);

    List<UserInfoDocument> selectForCondition(@Param(value="redisTime") LocalDateTime publishTime,@Param(value="nowTime") LocalDateTime nowTime);

    List<Long> getFollowedUserIds(@Param(value = "localId") Long localUserId, @Param(value = "userIds")List<Long> userIds);
}
