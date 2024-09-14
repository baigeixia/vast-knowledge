package com.vk.comment.listener;

import com.alibaba.fastjson2.JSON;
import com.vk.comment.domain.ApComment;
import com.vk.comment.domain.ApCommentRepay;
import com.vk.comment.mapper.ApCommentMapper;
import com.vk.comment.mapper.ApCommentRepayMapper;
import com.vk.common.core.utils.StringUtils;
import com.vk.common.core.utils.threads.TaskVirtualExecutorUtil;
import com.vk.common.mq.common.MqConstants;
import com.vk.common.mq.domain.CommentVisitStreamMess;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import static com.vk.common.mq.common.MqConstants.COMMENT_NOTIFY_GROUP;
import static com.vk.common.mq.common.MqConstants.NOTIFY_GROUP;

@Component
@Slf4j(topic = "CommentLikeListener")
public class CommentLikeListener {

    @Autowired
    private ApCommentMapper apCommentMapper;
    @Autowired
    private ApCommentRepayMapper apCommentRepayMapper;

    @KafkaListener(topics = MqConstants.TopicCS.NEWS_COMMENT_INCR_HANDLE_TOPIC, groupId = NOTIFY_GROUP)
    public void upOrDown(ConsumerRecord<String, String> record) {
        log.warn("NEWS_COMMENT_INCR_HANDLE_TOPIC 开始");
        int p = record.partition();
        long o = record.offset();
        String jsonString = record.value();
        CommentVisitStreamMess comments = JSON.parseObject(jsonString, CommentVisitStreamMess.class);
        if (!ObjectUtils.isEmpty(comments)) {
            TaskVirtualExecutorUtil.executeWith(() -> {
                Long id = comments.getCommentId();
                if (!StringUtils.isLongEmpty(id)) {
                    ApComment comment = apCommentMapper.selectOneById(id);
                    Long like = comments.getLike();
                    if (null != comment) {
                        ApComment upComment = new ApComment();
                        upComment.setId(id);
                        upComment.setLikes(comment.getLikes() + like);
                        apCommentMapper.update(upComment);
                    } else {
                        ApCommentRepay commentRepay = apCommentRepayMapper.selectOneById(id);
                        if (null != commentRepay) {
                            ApCommentRepay repay = new ApCommentRepay();
                            repay.setId(id);
                            repay.setLikes(commentRepay.getLikes() + like);
                            apCommentRepayMapper.update(repay);
                        } else {
                            log.error("评论流保存错误：--> 评论信息查询为空");
                        }
                    }
                } else {
                    log.error("评论流保存错误：--> 评论id为空");
                }
            });
        } else {
            log.error("评论流保存错误：--> 评论信息为空");
        }
        log.warn("NEWS_COMMENT_INCR_HANDLE_TOPIC 结束");
    }
// log.info("/n p："+p+"/n o："+o+"/n jsonString："+jsonString);


}
