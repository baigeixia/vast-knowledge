package com.vk.user.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.vk.common.core.exception.LeadNewsException;
import com.vk.common.core.utils.RequestContextUtil;
import com.vk.user.domain.ApUser;
import com.vk.user.domain.AuthorInfo;
import com.vk.user.domain.vo.LocalUserInfoVo;
import com.vk.user.domain.vo.UserInfoVo;
import com.vk.user.mapper.ApUserMapper;
import com.vk.user.service.ApUserService;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.*;

/**
 * APP用户信息 服务层实现。
 *
 * @author 张三
 * @since 2024-05-13
 */
@Service
public class ApUserServiceImpl extends ServiceImpl<ApUserMapper, ApUser> implements ApUserService {

    @Override
    public Map<Long, AuthorInfo> getUserList(Set<Long> userId) {
        Map<Long, AuthorInfo> infoMap = new HashMap<>();

        if (!ObjectUtils.isEmpty(userId)){
            List<ApUser> users = mapper.selectListByIds(userId);
            for (ApUser i : users) {
                AuthorInfo info = new AuthorInfo();
                info.setId(i.getId());
                info.setUsername(i.getName());
                info.setPosition(i.getPosition());
                info.setAvatar(i.getImage());
                infoMap.put(i.getId(),info);
            }
        }


        return infoMap;
    }

    @Override
    public UserInfoVo getInfo(Long id) {
        ApUser apUser = mapper.selectOneById(id);
        if (null==apUser){
            throw  new LeadNewsException("错误的用户");
        }
        return  mapper.selectGetInfo(id);
    }

    @Override
    public LocalUserInfoVo getLocalInfo() {
        Long userid = RequestContextUtil.getUserId();
        LocalUserInfoVo vo = mapper.selectGetLocalInfo(userid);
        vo.setPhone(maskPhoneNumber(vo.getPhone()));
        return vo;
    }

    public static String maskPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.length() < 7) {
            return phoneNumber; // 如果手机号过短，直接返回
        }
        // 使用正则表达式替换手机号中间的部分为星号
        return phoneNumber.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    }
}
