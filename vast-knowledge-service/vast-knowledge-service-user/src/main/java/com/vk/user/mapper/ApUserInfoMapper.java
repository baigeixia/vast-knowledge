package com.vk.user.mapper;

import com.mybatisflex.core.BaseMapper;
import com.vk.user.domain.ApUserInfo;
import com.vk.user.domain.vo.UserInfoVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

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

}
