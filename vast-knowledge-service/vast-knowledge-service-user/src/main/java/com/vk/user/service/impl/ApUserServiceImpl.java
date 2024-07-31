package com.vk.user.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.vk.user.domain.ApUser;
import com.vk.user.domain.AuthorInfo;
import com.vk.user.mapper.ApUserMapper;
import com.vk.user.service.ApUserService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * APP用户信息 服务层实现。
 *
 * @author 张三
 * @since 2024-05-13
 */
@Service
public class ApUserServiceImpl extends ServiceImpl<ApUserMapper, ApUser> implements ApUserService {

    @Override
    public List<Map<Long, AuthorInfo>>  getUserList(Set<Long> userId) {
       List<Map<Long, AuthorInfo>> infoMap = new ArrayList<>();

        if (null!=userId){
            List<ApUser> users = mapper.selectListByIds(userId);
            infoMap = users.stream().map(i -> {
                AuthorInfo info = new AuthorInfo();
                info.setId(i.getId());
                info.setUsername(i.getName());
                info.setPosition(i.getPosition());
                info.setAvatar(i.getImage());

                return Map.of(i.getId(), info);
            }).toList();
        }


        return infoMap;
    }
}
