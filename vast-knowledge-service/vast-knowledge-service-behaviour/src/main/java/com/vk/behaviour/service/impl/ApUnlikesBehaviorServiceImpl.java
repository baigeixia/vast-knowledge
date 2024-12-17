package com.vk.behaviour.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.vk.behaviour.domain.ApUnlikesBehavior;
import com.vk.behaviour.mapper.ApUnlikesBehaviorMapper;
import com.vk.behaviour.service.ApUnlikesBehaviorService;
import com.vk.common.core.exception.LeadNewsException;
import com.vk.common.core.utils.RequestContextUtil;
import com.vk.common.core.utils.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * APP不喜欢行为 服务层实现。
 *
 * @author 张三
 * @since 2024-05-13
 */
@Service
public class ApUnlikesBehaviorServiceImpl extends ServiceImpl<ApUnlikesBehaviorMapper, ApUnlikesBehavior> implements ApUnlikesBehaviorService {

    @Override
    public void saveUnlike(Long id) {
        Long userId = RequestContextUtil.getUserId();
        if (StringUtils.isLongEmpty(id)){
            throw new LeadNewsException("错误的文章");
        }

        Long isOne= mapper.getOne(id);
        if (!StringUtils.isLongEmpty(isOne)){
            throw new LeadNewsException("已经是不喜欢文章");
        }

        ApUnlikesBehavior behavior = new ApUnlikesBehavior();
        behavior.setEntryId(userId);
        behavior.setArticleId(id);
        behavior.setType(0);
        behavior.setCreatedTime(LocalDateTime.now());
        mapper.insert(behavior);
    }
}
