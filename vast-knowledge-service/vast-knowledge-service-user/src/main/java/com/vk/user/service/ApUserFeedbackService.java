package com.vk.user.service;

import com.mybatisflex.core.service.IService;
import com.vk.user.domain.ApUserFeedback;
import com.vk.user.domain.dto.FeedbackDto;

/**
 * APP用户反馈信息 服务层。
 *
 * @author 张三
 * @since 2024-05-13
 */
public interface ApUserFeedbackService extends IService<ApUserFeedback> {

    void feedbackSave(FeedbackDto dto);
}
