package com.vk.user.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.vk.common.core.utils.RequestContextUtil;
import com.vk.user.domain.ApUserMessage;
import com.vk.user.domain.vo.SysteamNotificationListVo;
import com.vk.user.mapper.ApUserMessageMapper;
import com.vk.user.service.ApUserMessageService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * APP用户消息通知信息 服务层实现。
 *
 * @author 张三
 * @since 2024-05-13
 */
@Service
public class ApUserMessageServiceImpl extends ServiceImpl<ApUserMessageMapper, ApUserMessage> implements ApUserMessageService {

    @Override
    public List<SysteamNotificationListVo> getSystemList(Long page, Long size) {
        ArrayList<SysteamNotificationListVo> vos = new ArrayList<>();
        Long userId = RequestContextUtil.getUserId();


        return vos;
    }
}
