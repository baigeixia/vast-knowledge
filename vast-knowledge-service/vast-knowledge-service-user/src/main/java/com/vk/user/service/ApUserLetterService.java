package com.vk.user.service;

import com.mybatisflex.core.service.IService;
import com.vk.user.domain.ApUserLetter;
import com.vk.user.domain.vo.MsgListVo;
import com.vk.user.domain.vo.MsgUserListVo;

import java.util.List;

/**
 * APP用户私信信息 服务层。
 *
 * @author 张三
 * @since 2024-05-13
 */
public interface ApUserLetterService extends IService<ApUserLetter> {

    List<MsgUserListVo> letterList(Integer type, Integer page, Integer size);

    MsgListVo msgList(Long userId, Integer page, Integer size);

    void clearUnreadMsg(Long userId);

    void delMsg(Long msgId);
}
