package com.vk.user.service;

import com.mybatisflex.core.service.IService;
import com.vk.user.domain.ApUserInfo;
import com.vk.user.domain.dto.UserInfoDto;
import com.vk.user.domain.vo.InfoRelationVo;
import com.vk.user.domain.vo.LocalUserInfoVo;
import com.vk.user.domain.vo.UserInfoVo;

/**
 * APP用户详情信息 服务层。
 *
 * @author 张三
 * @since 2024-05-13
 */
public interface ApUserInfoService extends IService<ApUserInfo> {



    void userConfig(Integer state, Integer type);

    UserInfoVo getInfo(Long id);

    void upInfo(UserInfoDto dto);

    InfoRelationVo InfoRelation(Long id);
}
