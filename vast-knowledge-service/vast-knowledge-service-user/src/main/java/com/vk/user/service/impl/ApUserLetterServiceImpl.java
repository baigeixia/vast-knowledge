package com.vk.user.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.vk.common.core.constant.UserBehaviourConstants;
import com.vk.common.core.domain.ValidationUtils;
import com.vk.common.core.exception.LeadNewsException;
import com.vk.common.core.utils.RequestContextUtil;
import com.vk.user.domain.ApUserLetter;
import com.vk.user.domain.AuthorInfo;
import com.vk.user.domain.vo.MsgUserListVo;
import com.vk.user.mapper.ApUserLetterMapper;
import com.vk.user.service.ApUserLetterService;
import com.vk.user.service.ApUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * APP用户私信信息 服务层实现。
 *
 * @author 张三
 * @since 2024-05-13
 */
@Service
@Slf4j
public class ApUserLetterServiceImpl extends ServiceImpl<ApUserLetterMapper, ApUserLetter> implements ApUserLetterService {

@Autowired
private ApUserService apUserService;
    @Override
    public List<MsgUserListVo> letterList(Integer type, Integer page, Integer size) {
        ArrayList<MsgUserListVo> vos = new ArrayList<>();
        Long userId = RequestContextUtil.getUserId();
        page=(page - 1)*size;
        if (type.equals(UserBehaviourConstants.USER_MSG_TYPE_RECENT)){
            //最新
            vos = mapper.getRecentList(userId,page,size);
        }else if (type.equals(UserBehaviourConstants.USER_MSG_TYPE_STRANGER)){
            //陌生
            vos = mapper.getStrangerList(userId,page,size);
        }else if(type.equals(UserBehaviourConstants.USER_MSG_TYPE_MUTUAL_CONCERN)){
            //互相关注
            vos = mapper.getMutualConcernList(userId,page,size);
        }else {
            log.error("用户联系人  type 错误");
            throw  new LeadNewsException("查询用户联系人错误");
        }

        Set<Long> userSet = vos.stream().map(i -> {
            if (null == i.getUnread()) {
                i.setUnread(UserBehaviourConstants.USER_READ_NO);
            }

            return i.getId();
        }).collect(Collectors.toSet());

        Map<Long, AuthorInfo> userList = apUserService.getUserList(userSet);
        if (null==userList){
            log.error("查询用户列表失败");
            throw new LeadNewsException("服务错误");
        }

        for (MsgUserListVo vo : vos) {
            Long id = vo.getId();
            AuthorInfo authorInfo = userList.get(id);

            if (authorInfo != null) {
                vo.setName(authorInfo.getUsername());
                vo.setAvatar(authorInfo.getAvatar());
            } else {
                log.warn("没有找到这个 用户id信息 {} ", id);
            }
        }
        return vos;
    }
}
