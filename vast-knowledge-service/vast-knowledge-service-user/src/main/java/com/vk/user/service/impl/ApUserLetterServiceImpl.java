package com.vk.user.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.vk.common.core.constant.UserBehaviourConstants;
import com.vk.common.core.exception.CustomSimpleThrowUtils;
import com.vk.common.core.exception.LeadNewsException;
import com.vk.common.core.utils.RequestContextUtil;
import com.vk.user.domain.ApUser;
import com.vk.user.domain.ApUserLetter;
import com.vk.user.domain.AuthorInfo;
import com.vk.user.domain.vo.MsgInfo;
import com.vk.user.domain.vo.MsgListVo;
import com.vk.user.domain.vo.MsgUserInfo;
import com.vk.user.domain.vo.MsgUserListVo;
import com.vk.user.mapper.ApUserLetterMapper;
import com.vk.user.service.ApUserLetterService;
import com.vk.user.service.ApUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;
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
        page = (page - 1) * size;
        if (type.equals(UserBehaviourConstants.USER_MSG_TYPE_RECENT)) {
            // 最新
            vos = mapper.getRecentList(userId, page, size);
        } else if (type.equals(UserBehaviourConstants.USER_MSG_TYPE_STRANGER)) {
            // 陌生
            vos = mapper.getStrangerList(userId, page, size);
        } else if (type.equals(UserBehaviourConstants.USER_MSG_TYPE_MUTUAL_CONCERN)) {
            // 互相关注
            vos = mapper.getMutualConcernList(userId, page, size);
        } else {
            log.error("用户联系人  type 错误");
            throw new LeadNewsException("查询用户联系人错误");
        }

        Set<Long> userSet = vos.stream().map(i -> {
            if (null == i.getUnread()) {
                i.setUnread(UserBehaviourConstants.USER_READ_NO);
            }
            return i.getId();
        }).collect(Collectors.toSet());

        Map<Long, AuthorInfo> userList = apUserService.getUserList(userSet);
        if (null == userList) {
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

    @Override
    public MsgListVo msgList(Long userId, Integer page, Integer size) {
        ApUser receiver = apUserService.getById(userId);
        CustomSimpleThrowUtils.ObjectIsEmpty(receiver, "错误的receiver用户");

        Long localUserId = RequestContextUtil.getUserId();
        ApUser sender = apUserService.getById(localUserId);
        CustomSimpleThrowUtils.ObjectIsEmpty(sender, "错误的sender用户");
        page = (page - 1) * size;

        MsgListVo vo = new MsgListVo();
        List<MsgInfo> infos = mapper.getMsgList(userId, localUserId, page, size);
        Collections.reverse(infos);
        vo.setMessages(getMsgListWithTimestamps(infos));
        vo.setSender(new MsgUserInfo(sender.getId(), sender.getName(), sender.getImage()));
        vo.setReceiver(new MsgUserInfo(receiver.getId(), receiver.getName(), receiver.getImage()));

        return vo;
    }

    @Override
    public void clearUnreadMsg(Long userId) {
        Long localUserId = RequestContextUtil.getUserId();
        mapper.clearUnreadMsg(userId, localUserId);
    }

    @Override
    public void delMsg(Long msgId) {
        Long localUserId = RequestContextUtil.getUserId();
        Long statusPointing = mapper.selectUserIdAndMsgId(localUserId, msgId);
        if(null==statusPointing){
            throw new LeadNewsException("错误的消息");
        }

        if (statusPointing.equals(0L)) {
            //无状态添加删除用户id
            mapper.deleteMsgId(localUserId, msgId);
        }else if(statusPointing.equals(110L)){
            throw new LeadNewsException("消息已删除");
        }else {
            //对方已经删除消息 改为全部移除
            mapper.deleteMsgAll(localUserId, msgId);
        }
    }

    private static final Duration TIME_INTERVAL = Duration.ofMinutes(10); // 10 分钟

    public List<MsgInfo> getMsgListWithTimestamps(List<MsgInfo> infos) {
        // 添加时间戳显示标志
        List<MsgInfo> processedMessages = new ArrayList<>();
        if (infos.isEmpty()) {
            return processedMessages;
        }

        // 添加第一条消息（总是显示时间戳）
        MsgInfo firstMessage = infos.get(0);
        firstMessage.setShowTimestamp(true);
        processedMessages.add(firstMessage);

        // 遍历后续消息
        for (int i = 1; i < infos.size(); i++) {
            MsgInfo currentMessage = infos.get(i);
            MsgInfo previousMessage = infos.get(i - 1);

            Duration timeDiff = Duration.between(previousMessage.getCreatedTime(), currentMessage.getCreatedTime());
            boolean showTimestamp = timeDiff.compareTo(TIME_INTERVAL) >= 0;

            currentMessage.setShowTimestamp(showTimestamp);
            processedMessages.add(currentMessage);
        }

        return processedMessages;
    }
}
