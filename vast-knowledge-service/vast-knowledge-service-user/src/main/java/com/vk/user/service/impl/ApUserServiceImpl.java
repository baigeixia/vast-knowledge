package com.vk.user.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.vk.common.core.exception.LeadNewsException;
import com.vk.common.core.utils.RequestContextUtil;
import com.vk.common.redis.service.RedisService;
import com.vk.user.common.constant.UserConstants;
import com.vk.user.domain.ApUser;
import com.vk.user.domain.AuthorInfo;
import com.vk.user.domain.vo.LocalUserInfoVo;
import com.vk.user.domain.vo.UserInfoVo;
import com.vk.user.mapper.ApUserMapper;
import com.vk.user.service.ApUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.*;

import static com.vk.user.common.constant.UserConstants.redisUserInfoKey;

/**
 * APP用户信息 服务层实现。
 *
 * @author 张三
 * @since 2024-05-13
 */
@Service
public class ApUserServiceImpl extends ServiceImpl<ApUserMapper, ApUser> implements ApUserService {

    @Autowired
    private RedisService redisService;

    @Override
    public Map<Long, AuthorInfo> getUserList(Set<Long> userId) {
        Map<Long, AuthorInfo> infoMap = new HashMap<>();

        if (!ObjectUtils.isEmpty(userId)){
            List<ApUser> users = mapper.selectListByIds(userId);
            for (ApUser i : users) {
                AuthorInfo info = new AuthorInfo();
                info.setId(i.getId());
                info.setUsername(i.getName());
                info.setAvatar(i.getImage());
                infoMap.put(i.getId(),info);
            }
        }


        return infoMap;
    }



}
