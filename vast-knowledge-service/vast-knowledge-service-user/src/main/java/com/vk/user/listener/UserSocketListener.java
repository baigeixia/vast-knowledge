package com.vk.user.listener;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.nacos.common.utils.UuidUtils;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.row.Db;
import com.vk.common.core.utils.threads.TaskVirtualExecutorUtil;
import com.vk.common.mq.common.MqConstants;
import com.vk.common.mq.domain.NewUserMsg;
import com.vk.user.domain.ApUserFan;
import com.vk.user.domain.ApUserFollow;
import com.vk.user.domain.ApUserLetter;
import com.vk.user.domain.ApUserMessage;
import com.vk.user.mapper.ApUserFanMapper;
import com.vk.user.mapper.ApUserFollowMapper;
import com.vk.user.mapper.ApUserLetterMapper;
import com.vk.user.mapper.ApUserMessageMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;

import static com.vk.common.mq.common.MqConstants.SocketType.*;
import static com.vk.user.domain.table.ApUserFanTableDef.AP_USER_FAN;
import static com.vk.user.domain.table.ApUserFollowTableDef.AP_USER_FOLLOW;
import static com.vk.user.domain.table.ApUserMessageTableDef.AP_USER_MESSAGE;

@Component
@Slf4j(topic = "UserSocketListener")
public class UserSocketListener {

    @Autowired
    private ApUserMessageMapper apUserMessageMapper;

    @Autowired
    private ApUserFollowMapper apUserFollowMapper;

    @Autowired
    private ApUserFanMapper apUserFanMapper;

    @Autowired
    private ApUserLetterMapper apUserLetterMapper;


    @KafkaListener(topics = MqConstants.TopicCS.NEWS_USER_MESSAGE_TOPIC, groupId = MqConstants.NOTIFY_GROUP)
    public void upOrDown(ConsumerRecord<String, String> record) {
        int p = record.partition();
        long o = record.offset();
        String jsonString = record.value();
        NewUserMsg userMsg = JSONObject.parseObject(jsonString, NewUserMsg.class);
        if (!ObjectUtils.isEmpty(userMsg)) {


            String uuid = UuidUtils.generateUuid();
            Integer type = userMsg.getType();
            switch (type){
                case FOLLOW:
                    TaskVirtualExecutorUtil.executeWith(() -> {
                        log.info("FOLLOW 关注 mark:{}",uuid);
                        followOrFanAdd(userMsg);
                        userLetterAdd(userMsg);
                    });
                    break;
                case FOLLOW_NO:
                    TaskVirtualExecutorUtil.executeWith(() -> {
                        log.info("FOLLOW_NO 取消关注 mark:{}",uuid);
                        followOrFanDel(userMsg);
                        userMessageDel(userMsg);
                    });
                    break;
                case LIKE:
                    TaskVirtualExecutorUtil.executeWith(() -> {
                        log.info("LIKE 点赞文章 mark:{}",uuid);
                        userMessageAdd(userMsg);
                    });
                    break;
                case LIKE_NO:
                    TaskVirtualExecutorUtil.executeWith(() -> {
                        log.info("LIKE_NO 取消点赞文章 mark:{}",uuid);
                        userMessageDel(userMsg);
                    });
                    break;
                case FORWARD:
                    TaskVirtualExecutorUtil.executeWith(() -> {
                        log.info("FORWARD 转发文章  mark:{}",uuid);
                        userMessageAdd(userMsg);
                    });
                    break;
                case LIKE_COMMENT:
                    TaskVirtualExecutorUtil.executeWith(() -> {
                        log.info("LIKE_COMMENT 点赞评论 mark:{}",uuid);
                        userMessageAdd(userMsg);
                    });
                    break;
                case LIKE_COMMENT_NO:
                    TaskVirtualExecutorUtil.executeWith(() -> {
                        log.info("LIKE_COMMENT_NO 取消评论点赞 mark:{}",uuid);
                        userMessageDel(userMsg);
                    });
                    break;
                case CHAT_MSG:
                    TaskVirtualExecutorUtil.executeWith(() -> {
                        log.info("CHAT_MSG 私信通知 mark:{}",uuid);
                        userLetterAdd(userMsg);
                        userMessageAdd(userMsg);
                    });
                    break;
                case COMMENT:
                    TaskVirtualExecutorUtil.executeWith(() -> {
                        log.info("COLLECT 评论通知 mark:{}",uuid);
                        userMessageAdd(userMsg);
                    });
                    break;
                case SHARE:
                    TaskVirtualExecutorUtil.executeWith(() -> {
                        log.info("COLLECT 分享通知 mark:{}",uuid);
                        userMessageAdd(userMsg);
                    });
                    break;
                default:
                    log.error("NEWS_LIKE_TOPIC 没有对应类型错误");
            }

        }else {
            log.error("错误的 userMsg 为空");
        }


    }

    private void userMessageDel(NewUserMsg userMsg) {
        ApUserMessage message = apUserMessageMapper.selectOneByQuery(
                QueryWrapper.create()
                        .where(AP_USER_MESSAGE.USER_ID.eq(userMsg.getUserId())
                                .and(AP_USER_MESSAGE.SENDER_ID.eq(userMsg.getSenderId()))
                                .and(AP_USER_MESSAGE.IS_READ.eq(false))
                        )
        );

        if(!ObjectUtils.isEmpty(message)){
            apUserMessageMapper.deleteById(message.getId());
        }
    }

    private void userLetterAdd(NewUserMsg userMsg) {
            ApUserLetter letter = new ApUserLetter();
            letter.setUserId(userMsg.getUserId());
            letter.setSenderId(userMsg.getSenderId());
            letter.setSenderName(userMsg.getSenderName());
            letter.setContent(userMsg.getContent());
            letter.setCreatedTime(LocalDateTime.now());
            apUserLetterMapper.insert(letter);
    }

    private void userMessageAdd(NewUserMsg userMsg) {
        ApUserMessage message = new ApUserMessage();
        BeanUtils.copyProperties(userMsg,message);
        message.setCreatedTime(LocalDateTime.now());
        apUserMessageMapper.insert(message);
    }

    private void followOrFanDel(NewUserMsg userMsg) {
        Db.tx(()->{
            ApUserFollow follow = apUserFollowMapper.selectOneByQuery(QueryWrapper.create()
                    .where(AP_USER_FOLLOW.USER_ID.eq(userMsg.getUserId())
                            .and(AP_USER_FOLLOW.FOLLOW_ID.eq(userMsg.getSenderId()))));
            if(!ObjectUtils.isEmpty(follow)){
                apUserFollowMapper.deleteById(follow.getId());
            }

            ApUserFan fan = apUserFanMapper.selectOneByQuery(QueryWrapper.create()
                    .where(AP_USER_FAN.USER_ID.eq(userMsg.getSenderId()))
                    .and(AP_USER_FAN.FANS_ID.eq(userMsg.getUserId())));

            if(!ObjectUtils.isEmpty(fan)){
                apUserFanMapper.deleteById(fan.getId());
            }
            return true;
        });

    }

    private void followOrFanAdd(NewUserMsg userMsg) {
        Db.tx(()->{
            ApUserFollow follow = new ApUserFollow();
            follow.setUserId(userMsg.getUserId());
            follow.setFollowId(userMsg.getSenderId());
            follow.setFollowName(userMsg.getSenderName());
            follow.setLevel(0);
            follow.setIsNotice(1);
            follow.setCreatedTime(LocalDateTime.now());
            apUserFollowMapper.insert(follow);

            ApUserFan fan = new ApUserFan();
            fan.setUserId(userMsg.getSenderId());
            fan.setFansId(userMsg.getUserId());
            fan.setFansName(userMsg.getUserName());
            fan.setLevel(0);
            fan.setCreatedTime(LocalDateTime.now());
            fan.setIsDisplay(1);
            fan.setIsShieldComment(1);
            fan.setIsShieldLetter(0);
            apUserFanMapper.insert(fan);

            return true;
        });

    }


}
