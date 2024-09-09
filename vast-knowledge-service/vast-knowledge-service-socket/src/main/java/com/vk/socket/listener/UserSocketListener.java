package com.vk.socket.listener;

import com.alibaba.fastjson2.JSONObject;
import com.corundumstudio.socketio.SocketIOServer;
import com.vk.common.mq.common.MqConstants;
import com.vk.common.mq.domain.NewUserMsg;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j(topic ="UserSocketListener" )
public class UserSocketListener {


    @Autowired
    private  SocketIOServer socketIOServer;


    // @KafkaListener(topics = MqConstants.TopicCS.NEWS_LIKE_TOPIC,groupId = MqConstants.NOTIFY_GROUP)
    public void upOrDown(ConsumerRecord<String,String> record){
        int p = record.partition();
        long o = record.offset();
        String jsonString = record.value();
        NewUserMsg newUserMsg = JSONObject.parseObject(jsonString, NewUserMsg.class);
        // Long notifyUserId = newUserMsg.getNotifyUserId();
        // Integer messageType = newUserMsg.getMessageType();

        System.out.println(p);
        System.out.println(o);
        System.out.println(jsonString);
        // System.out.println(notifyUserId);
        // System.out.println(messageType);
        //手动提交offset
        //转对象
        // socketIOServer.getClient(UUID.fromString(userid)).sendEvent(MqConstants.UserSocketCS.NEW_LIKE);
    }



}
