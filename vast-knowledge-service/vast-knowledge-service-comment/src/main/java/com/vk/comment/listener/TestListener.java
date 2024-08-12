package com.vk.comment.listener;

import com.vk.common.mq.common.MqConstants;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TestListener {

        @KafkaListener(topics = MqConstants.TopicCS.HOT_ARTICLE_SCORE_TOPIC,groupId = "test-group")
        public void upOrDown(ConsumerRecord<String,String> record){
            int p = record.partition();
            long o = record.offset();
            String jsonString = record.value();
            System.out.println("/n p："+p+"/n o："+o+"/n jsonString："+jsonString);
        }

}
