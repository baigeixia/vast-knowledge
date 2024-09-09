package com.vk.user.service;

import com.mybatisflex.core.service.IService;
import com.vk.user.domain.ApUserMessage;
import com.vk.user.domain.vo.SysteamNotificationListVo;

import java.util.List;

/**
 * APP用户消息通知信息 服务层。
 *
 * @author 张三
 * @since 2024-05-13
 */
public interface ApUserMessageService extends IService<ApUserMessage> {

    List<SysteamNotificationListVo> getSystemList(Long page, Long size);
}
