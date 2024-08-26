package com.vk.socket.listener;

import com.corundumstudio.socketio.SocketIOServer;
import com.vk.common.mq.common.MqConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j(topic ="UserSocketListener" )
public class UserSocketListener {


    @Autowired
    private  SocketIOServer socketIOServer;

    // @KafkaListener(topics = MqConstants.TopicCS.HOT_ARTICLE_SCORE_TOPIC,groupId = "test-group")
    //     public void upOrDown(ConsumerRecord<String,String> record){
    //         int p = record.partition();
    //         long o = record.offset();
    //         String jsonString = record.value();
    //         log.info("/n p："+p+"/n o："+o+"/n jsonString："+jsonString);
    //     }

    @KafkaListener(topics = MqConstants.TopicCS.NEWS_LIKE_TOPIC,groupId = MqConstants.NOTIFY_GROUP)
    public void upOrDown(ConsumerRecord<String,String> record){
        int p = record.partition();
        long o = record.offset();
        // String jsonString = record.value();
        String userid = record.value();
        //转对象
        socketIOServer.getClient(UUID.fromString(userid)).sendEvent(MqConstants.UserSocketCS.NEWS_LIKE);
    }



}
