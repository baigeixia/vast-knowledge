package com.vk.comment.controller;

import com.vk.common.mq.common.MqConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class TestService {

    @Autowired
    private KafkaTemplate kafkaTemplate;
    @Async
    public void test(){
        CompletableFuture hello = kafkaTemplate.send(MqConstants.TopicCS.HOT_ARTICLE_SCORE_TOPIC, "hello");
    }
    
}
