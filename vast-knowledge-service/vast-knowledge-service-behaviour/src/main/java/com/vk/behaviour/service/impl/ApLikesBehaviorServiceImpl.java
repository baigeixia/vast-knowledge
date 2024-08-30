package com.vk.behaviour.service.impl;

import com.alibaba.fastjson2.JSON;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.vk.behaviour.domain.ApLikesBehavior;
import com.vk.behaviour.domain.dto.LikesBehaviorDto;
import com.vk.behaviour.mapper.ApLikesBehaviorMapper;
import com.vk.behaviour.service.ApLikesBehaviorService;
import com.vk.common.core.constant.CollectionConstants;
import com.vk.common.core.exception.LeadNewsException;
import com.vk.common.core.utils.RequestContextUtil;
import com.vk.common.core.utils.StringUtils;
import com.vk.common.core.utils.bean.BeanUtils;
import com.vk.common.mq.common.MqConstants;
import com.vk.common.mq.domain.NewMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * APP点赞行为 服务层实现。
 *
 * @author 张三
 * @since 2024-05-13
 */
@Service
@Slf4j
public class ApLikesBehaviorServiceImpl extends ServiceImpl<ApLikesBehaviorMapper, ApLikesBehavior> implements ApLikesBehaviorService {

    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;
    @Override
    public void saveLike(LikesBehaviorDto dto) {
        Long userId = RequestContextUtil.getUserId();
        String userName = RequestContextUtil.getUserName();

        Long id = dto.getId();
        Long articleId = dto.getArticleId();
        Long repayAuthorId = dto.getRepayAuthorId();
        Integer operation = dto.getOperation();
        Integer type = dto.getType();

        if (operation == CollectionConstants.LIKE_NO){
            if (StringUtils.isLongEmpty(id)) {
                throw new LeadNewsException("错误的参数");
            }
            ApLikesBehavior behavior = new ApLikesBehavior();
            behavior.setId(id);
            behavior.setOperation(CollectionConstants.LIKE_NO);
            mapper.update(behavior);
        }else {
            ApLikesBehavior behavior = new ApLikesBehavior();
            BeanUtils.copyProperties(dto,behavior);
            behavior.setAuthorId(userId);
            behavior.setAuthorName(userName);
            behavior.setCreatedTime(LocalDateTime.now());
            mapper.insert(behavior);

            try {
                NewMsg newMsg = new NewMsg(repayAuthorId,MqConstants.UserSocketCS.NEWS_LIKE);
                kafkaTemplate.send(MqConstants.TopicCS.NEWS_LIKE_TOPIC, JSON.toJSONString(newMsg));
            } catch (Exception e) {
                log.error("点赞行为发送kafka失败 id: {} 错误:{}",id,e.getMessage());
            }

        }
    }
}
