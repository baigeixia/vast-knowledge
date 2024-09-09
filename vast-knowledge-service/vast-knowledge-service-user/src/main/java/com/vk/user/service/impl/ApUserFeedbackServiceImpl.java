package com.vk.user.service.impl;

import com.mybatisflex.core.row.Db;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.vk.common.core.exception.LeadNewsException;
import com.vk.common.core.utils.RequestContextUtil;
import com.vk.common.core.utils.StringUtils;
import com.vk.user.domain.ApUserFeedback;
import com.vk.user.domain.ApUserMessage;
import com.vk.user.domain.dto.FeedbackDto;
import com.vk.user.mapper.ApUserFeedbackMapper;
import com.vk.user.mapper.ApUserMessageMapper;
import com.vk.user.service.ApUserFeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;

/**
 * APP用户反馈信息 服务层实现。
 *
 * @author 张三
 * @since 2024-05-13
 */
@Service
public class ApUserFeedbackServiceImpl extends ServiceImpl<ApUserFeedbackMapper, ApUserFeedback> implements ApUserFeedbackService {

    @Autowired
    private ApUserMessageMapper apUserMessageMapper;
    @Override
    public void feedbackSave(FeedbackDto dto) {
        Long userId = RequestContextUtil.getUserId();
        String userName = RequestContextUtil.getUserName();

        if (ObjectUtils.isEmpty(dto)) {
            throw new LeadNewsException("错误的反馈");
        }

        String content = dto.getContent();
        if (StringUtils.isEmpty(content)) {
            throw new LeadNewsException("反馈描述不能为空");
        }

        ApUserFeedback feedback = new ApUserFeedback();
        feedback.setUserId(userId);
        feedback.setUserName(userName);
        feedback.setImages(dto.getImages());
        feedback.setContent(dto.getContent());
        feedback.setIsSolve(0);
        feedback.setCreatedTime(LocalDateTime.now());
        mapper.insert(feedback);
    }
}
