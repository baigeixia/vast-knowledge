package com.vk.user.service;

import com.mybatisflex.core.service.IService;
import com.vk.common.es.domain.UserInfoDocument;
import com.vk.user.domain.ApUserInfo;
import com.vk.user.domain.dto.UserInfoDto;
import com.vk.user.domain.vo.InfoRelationVo;
import com.vk.user.domain.vo.LocalUserInfoVo;
import com.vk.user.domain.vo.UserInfoVo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CountDownLatch;

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

    Long selectCount(LocalDateTime now);

    void importAll(long page, Long size, CountDownLatch countDownLatch, LocalDateTime now);

    List<UserInfoDocument> searchUser(String query, Integer type, Integer sort, Integer period, Long page, Long size);
}
