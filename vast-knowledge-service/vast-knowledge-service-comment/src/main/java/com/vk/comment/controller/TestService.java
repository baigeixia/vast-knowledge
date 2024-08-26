package com.vk.comment.controller;

import com.mybatisflex.core.query.QueryWrapper;
import com.vk.comment.document.ApCommentDocument;
import com.vk.comment.document.ApCommentRepayDocument;
import com.vk.comment.domain.table.ApCommentTableDef;
import com.vk.comment.repository.CommentDocumentRepository;
import com.vk.comment.repository.CommentRepayDocumentRepository;
import com.vk.comment.service.ApCommentRepayService;
import com.vk.comment.service.ApCommentService;
import com.vk.common.core.constant.DatabaseConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.vk.comment.domain.table.ApCommentRepayTableDef.AP_COMMENT_REPAY;

@Service
public class TestService {

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Autowired
    private CommentDocumentRepository commentDocumentRepository;

    @Autowired
    private CommentRepayDocumentRepository commentRepayDocumentRepository;

    @Autowired
    private ApCommentService apCommentService;

    @Autowired
    private ApCommentRepayService apCommentRepayService;


    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Async
    public void test(){
        // CompletableFuture hello = kafkaTemplate.send(MqConstants.TopicCS.HOT_ARTICLE_SCORE_TOPIC, "hello");
        List<ApCommentDocument> apCommentDocuments = apCommentService.listAs(QueryWrapper.create().select().where(ApCommentTableDef.AP_COMMENT.STATUS.eq(DatabaseConstants.DB_ROW_STATUS_YES)), ApCommentDocument.class);
        try {
            commentDocumentRepository.saveAll(apCommentDocuments);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Async
    public void testRe(){
        // CompletableFuture hello = kafkaTemplate.send(MqConstants.TopicCS.HOT_ARTICLE_SCORE_TOPIC, "hello");
        List<ApCommentRepayDocument> apCommentRepayDocuments = apCommentRepayService
                .listAs(QueryWrapper.create().select().where(AP_COMMENT_REPAY.STATUS.eq(DatabaseConstants.DB_ROW_STATUS_YES)), ApCommentRepayDocument.class);
        try {
            commentRepayDocumentRepository.saveAll(apCommentRepayDocuments);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public ApCommentDocument test2(Long id){
        // CompletableFuture hello = kafkaTemplate.send(MqConstants.TopicCS.HOT_ARTICLE_SCORE_TOPIC, "hello");
        try {
            Optional<ApCommentDocument> byId = commentDocumentRepository.findById(id);
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
