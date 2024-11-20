package com.vk.common.es.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.elasticsearch.config.ElasticsearchConfigurationSupport;
import org.springframework.data.elasticsearch.core.convert.ElasticsearchCustomConversions;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

/**
 * @author Ming
 * @date 2020/9/26 14:10
 */
@Configuration
public class ElasticsearchConfiguration extends ElasticsearchConfigurationSupport {



    // @Bean
    // @Override
    // @NonNull
    // public Elasti csearchCustomConversions  elasticsearchCustomConversions() {
    //     return  new ElasticsearchCustomConversions(
    //             Collections.singletonList(new LocalDateTimeToLongConverter())
    //             // Collections.singletonList(new LongToLocalDateTimeConverter())
    //             // Arrays.asList(
    //             //         new LocalDateTimeToLongConverter(),  // Add the converter here
    //             //         new LongToLocalDateTimeConverter()
    //             // )
    //     );
    // }



}

