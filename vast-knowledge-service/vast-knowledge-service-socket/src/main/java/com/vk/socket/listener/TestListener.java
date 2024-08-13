package com.vk.socket.listener;

import com.vk.common.mq.common.MqConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j(topic ="socket TestListener" )
public class TestListener {

        @KafkaListener(topics = MqConstants.TopicCS.HOT_ARTICLE_SCORE_TOPIC,groupId = "test-group")
        public void upOrDown(ConsumerRecord<String,String> record){
            int p = record.partition();
            long o = record.offset();
            String jsonString = record.value();
            log.info("/n p："+p+"/n o："+o+"/n jsonString："+jsonString);
        }

}
