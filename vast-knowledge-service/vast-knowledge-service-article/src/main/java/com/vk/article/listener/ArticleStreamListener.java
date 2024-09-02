package com.vk.article.listener;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.vk.common.mq.common.MqConstants;
import com.vk.common.mq.domain.ArticleVisitStreamMess;
import com.vk.common.mq.domain.NewMsg;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j(topic ="ArticleStreamListener" )
public class ArticleStreamListener {


    @KafkaListener(topics = MqConstants.TopicCS.HOT_ARTICLE_INCR_HANDLE_TOPIC,groupId = MqConstants.NOTIFY_GROUP)
    public void upOrDown(ConsumerRecord<String,String> record){
        int p = record.partition();
        long o = record.offset();
        String value = record.value();
        ArticleVisitStreamMess articleVisitStreamMess = JSON.parseObject(value, ArticleVisitStreamMess.class);
        System.out.println(articleVisitStreamMess.toString());
        // NewMsg newMsg = JSONObject.parseObject(jsonString, NewMsg.class);
        // Long notifyUserId = newMsg.getNotifyUserId();
        // Integer messageType = newMsg.getMessageType();

        System.out.println(p);
        System.out.println(o);
        System.out.println(value);
        // System.out.println(notifyUserId);
        // System.out.println(messageType);
        //手动提交offset
        //转对象
        // socketIOServer.getClient(UUID.fromString(userid)).sendEvent(MqConstants.UserSocketCS.NEWS_LIKE);
    }



}
