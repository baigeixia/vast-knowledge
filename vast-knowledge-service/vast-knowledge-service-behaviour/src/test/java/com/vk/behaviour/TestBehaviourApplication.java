package com.vk.behaviour;


import com.vk.behaviour.common.config.KafkaStreamsConfig;
import com.vk.behaviour.mapper.ApLikesBehaviorMapper;
import com.vk.behaviour.stream.HotArticleStreamHandler;
import com.vk.common.redis.configure.RedisConfig;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springdoc.webmvc.ui.SwaggerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest(classes = {TestBehaviourApplication.class})
public class TestBehaviourApplication {


    @Autowired
    private ApLikesBehaviorMapper apLikesBehaviorMapper;
    @Test
    void testLikeN(){
        Long page =1L;
        Long size =5L;

        List<String> dataTime= apLikesBehaviorMapper.getTimeRange((page-1)*size,size);
        System.out.println(dataTime);
    }
}
