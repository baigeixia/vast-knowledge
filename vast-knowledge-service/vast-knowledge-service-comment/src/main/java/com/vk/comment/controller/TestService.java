package com.vk.comment.controller;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import com.mybatisflex.core.query.QueryWrapper;
import com.vk.comment.document.ApCommentDocument;
import com.vk.comment.domain.table.ApCommentTableDef;
import com.vk.comment.repository.DocumentRepository;
import com.vk.comment.service.ApCommentService;
import com.vk.common.core.constant.DatabaseConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TestService {

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private ApCommentService apCommentService;


    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Async
    public void test(){
        // CompletableFuture hello = kafkaTemplate.send(MqConstants.TopicCS.HOT_ARTICLE_SCORE_TOPIC, "hello");
        List<ApCommentDocument> apCommentDocuments = apCommentService.listAs(QueryWrapper.create().select().where(ApCommentTableDef.AP_COMMENT.STATUS.eq(DatabaseConstants.DB_ROW_STATUS_YES)), ApCommentDocument.class);
        try {
            documentRepository.saveAll(apCommentDocuments);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public ApCommentDocument test2(Long id){
        // CompletableFuture hello = kafkaTemplate.send(MqConstants.TopicCS.HOT_ARTICLE_SCORE_TOPIC, "hello");
        try {
            Optional<ApCommentDocument> byId = documentRepository.findById(id);
            if (byId.isPresent()) {
                ApCommentDocument apCommentDocument = byId.get();
                System.out.println(apCommentDocument);
                // Instant createdTime = apCommentDocument.getCreatedTime();
                // LocalDateTime localDateTime = createdTime.atZone(ZoneId.systemDefault()).toLocalDateTime();
                // System.out.println("localDateTime :"+localDateTime);
                // Instant instant = Instant.ofEpochMilli(createdTime);
                // System.out.println("instant :"+instant);
                // System.out.println(TimeZone.getDefault().toZoneId());
                // LocalDateTime localDateTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
                // System.out.println("localDateTime :"+localDateTime);

                return apCommentDocument;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return null;
    }


}
