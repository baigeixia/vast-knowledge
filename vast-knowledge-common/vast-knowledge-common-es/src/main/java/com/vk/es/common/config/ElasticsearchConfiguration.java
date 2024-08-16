package com.vk.es.common.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.config.ElasticsearchConfigurationSupport;
import org.springframework.data.elasticsearch.core.convert.ElasticsearchCustomConversions;
import org.springframework.lang.NonNull;

import java.util.Collections;

/**
 * @author Ming
 * @date 2020/9/26 14:10
 */
@Configuration
public class ElasticsearchConfiguration extends ElasticsearchConfigurationSupport {

    @Bean
    @Override
    @NonNull
    public ElasticsearchCustomConversions  elasticsearchCustomConversions() {
        return  new ElasticsearchCustomConversions(
                Collections.singletonList(new LongToLocalDateTimeConverter())
        );
    }



}

