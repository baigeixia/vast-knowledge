package com.vk.common.mq.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * kafka的消费者配置
 *
*/

@Slf4j
@Component
public class KafkaProducerConfig {
    @Value("${spring.kafka.bootstrap-servers}")
    private String BROKERS;

    @Value("${spring.kafka.producer.retries}")
    private Integer RETRIES;

    @Value("${spring.kafka.producer.batch-size}")
    private Integer BATCH_SIZE;

    @Value("${spring.kafka.producer.buffer-memory}")
    private Long BUFFER_MEMORY;

    @Value("${spring.kafka.producer.acks}")
    private String ACK;

    @Value("${spring.kafka.producer.properties.max.block.ms}")
    private Integer BLOCK_MS;

    /**
     * 生产者配置
     *
     * @return 配置
     */
    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> properties = new HashMap<String, Object>();
        // kafka server地址
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BROKERS);
        properties.put(ProducerConfig.ACKS_CONFIG,ACK);
        // 消息发送失败重试次数
        properties.put(ProducerConfig.RETRIES_CONFIG, RETRIES);
        // 去缓冲区中一次拉16k的数据，发送到broker
        properties.put(ProducerConfig.BATCH_SIZE_CONFIG, BATCH_SIZE);
        // 设置缓存区大小 32m
        properties.put(ProducerConfig.BUFFER_MEMORY_CONFIG, BUFFER_MEMORY);
        // key序列化器选择，直接指定序列化包class,不能直接写 StringSerializer.class
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        // value序列化器选择
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer");
        // 设置sasl认证
        properties.put(SaslConfigs.SASL_MECHANISM, "PLAIN");
        properties.put(ProducerConfig.MAX_BLOCK_MS_CONFIG,BLOCK_MS);
        return properties;
    }

    /**
     * Producer Template 配置
     */
    @Bean
    public KafkaTemplate<String, String> kafkaTemplate(Map<String, Object>  producerConfigs) {
        // Map<String, Object> stringObjectMap = producerConfigs();
        DefaultKafkaProducerFactory<String, String> objectObjectDefaultKafkaProducerFactory = new DefaultKafkaProducerFactory<>(producerConfigs);
        return new KafkaTemplate<>(objectObjectDefaultKafkaProducerFactory);
    }
}