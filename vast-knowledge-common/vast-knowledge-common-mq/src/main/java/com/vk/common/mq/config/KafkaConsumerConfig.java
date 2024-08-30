package com.vk.common.mq.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * kafka的消费者配置
 *
 * @author G008186
 */

@Slf4j
@Component
public class KafkaConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String BROKERS;

    @Value("${spring.kafka.consumer.enable-auto-commit}")
    private Boolean ENABLE_AUTO_COMMIT;

    @Value("${spring.kafka.consumer.auto-commit-interval}")
    private String AUTO_COMMIT_INTERVAL_MS;

    @Value("${spring.kafka.consumer.auto-offset-reset}")
    private String AUTO_OFFSET_RESET;

    @Value("${spring.kafka.listener.concurrency}")
    private Integer CONCURRENCY;

    @Value("${spring.kafka.listener.missing-topics-fatal}")
    private Boolean TOPICS_FATAL;

    private String CURRENT_INSTANCE_GROUP_ID;
/**构建kafka监听工厂*/
    @Bean("kafkaListenerContainerFactory")
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<String, String>();
        // 这里配置监听器的模式，记得需要如下，先获取properties，然后才可以设置
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        factory.setConcurrency(CONCURRENCY);
        factory.setMissingTopicsFatal(TOPICS_FATAL);
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }


    /**初始化消费工厂配置 其中会动态指定消费分组*/
    private ConsumerFactory<String, String> consumerFactory() {
        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BROKERS);
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, ENABLE_AUTO_COMMIT);
        properties.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, 1);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        /*多实例部署每个实例设置不同groupId 实现发布订阅*/
        // CURRENT_INSTANCE_GROUP_ID = KafkaConstant.SSE_GROUP.concat(String.valueOf(System.identityHashCode(sendSyncTaskFactory)));
        log.info("当前实例WsMsgConsumer group_id:{}",CURRENT_INSTANCE_GROUP_ID);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, CURRENT_INSTANCE_GROUP_ID);
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, AUTO_OFFSET_RESET);

        return new DefaultKafkaConsumerFactory<String, String>(properties);
    }

}