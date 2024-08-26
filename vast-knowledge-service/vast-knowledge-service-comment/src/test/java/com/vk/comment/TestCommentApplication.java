package com.vk.comment;

import com.alibaba.fastjson2.JSON;
import com.mybatisflex.core.query.QueryWrapper;
import com.vk.comment.document.ApCommentDocument;
import com.vk.comment.document.ApCommentRepayDocument;
import com.vk.comment.domain.table.ApCommentRepayTableDef;
import com.vk.comment.domain.table.ApCommentTableDef;
import com.vk.comment.repository.CommentDocumentRepository;
import com.vk.comment.repository.CommentRepayDocumentRepository;
import com.vk.comment.service.ApCommentRepayService;
import com.vk.comment.service.ApCommentService;
import com.vk.common.core.constant.DatabaseConstants;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.vk.comment.domain.table.ApCommentRepayTableDef.AP_COMMENT_REPAY;


@SpringBootTest
public class TestCommentApplication {

    @Autowired
    private ApCommentService apCommentService;

    @Autowired
    private ApCommentRepayService commentRepayService;

    @Autowired
    private CommentDocumentRepository commentDocumentRepository;

    @Autowired
    private CommentRepayDocumentRepository commentRepayDocumentRepository;



    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    @Test
    void  testRepaySaveAll(){
        List<ApCommentRepayDocument> apCommentRepayDocuments = commentRepayService
                .listAs(QueryWrapper.create().select().where(AP_COMMENT_REPAY.STATUS.eq(DatabaseConstants.DB_ROW_STATUS_YES)), ApCommentRepayDocument.class);

        // apCommentRepayDocuments.forEach(System.out::println);
        commentRepayDocumentRepository.saveAll(apCommentRepayDocuments);
    }

    @Test
    void  testRepayGet(){
        List<Long> ids = List.of(42448706922000145L);
        Iterable<ApCommentRepayDocument> allById = commentRepayDocumentRepository.findAllById(ids);
        allById.forEach(System.out::println);
    }


    @Test
    void  testEsGetInfo(){
        Integer[] arId= {16,24,28};
        int page = 0; // 当前页码
        int size = 10; // 每页大小
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("createdTime")));
        List<ApCommentDocument> documents = commentDocumentRepository.findAllByEntryIdInOrderByCreatedTimeDesc(arId,pageable);
        // documents.forEach(System.out::println);

        Map<String, List<ApCommentDocument>> collect = documents.stream()
                .collect(Collectors.groupingBy(doc -> doc.getCreatedTime().toLocalDate().toString()));

        String jsonString = JSON.toJSONString(collect);
        System.out.println(jsonString);
        // collect.forEach((date, docs) -> {
        //     System.out.println("Date: " + date);
        //     docs.forEach(doc -> System.out.println("Document: " + doc));
        // });

    }
    @Test
    void  testComment(){
        long i=1722583380009L;
        long time = new Date().getTime();
        System.out.println(time-i);
        // CommentSaveDto saveDto = new CommentSaveDto();
        // saveDto.setContent("");
        // apCommentService.saveComment(saveDto);
    }


    @Test
    void  testComment1(){

        // ApCommentDocument commentDocument = elasticsearchOperations.get("42696889904000157", ApCommentDocument.class);
        // System.out.println(commentDocument);
        Criteria criteria = new Criteria("entryId").is(16)
                .subCriteria(
                new Criteria("id").is(1)
                        .or("id").is(6)
        );

        SearchHits<ApCommentDocument> search = elasticsearchOperations.search( new CriteriaQuery(criteria), ApCommentDocument.class);
        search.forEach(System.out::println);

    }

    @Test
    void  testComment2(){

        // SearchHits<ApCommentDocument> searchHits = commentDocumentRepository.findByEntryIdAndId(24L, 42696601722000119L, "重要");
        // searchHits.forEach(hit -> {
        //     // 打印原始文档内容
        //     System.out.println("Document: " + hit.getContent());
        //
        //     // 打印高亮字段
        //     hit.getHighlightFields().forEach((fieldName, highlight) -> {
        //         System.out.println("Field: " + fieldName);
        //         // System.out.println("Highlights: " + String.join(", ", highlight.fragments()));
        //     });
        // });

        SearchHits<ApCommentDocument> searchHits = commentDocumentRepository.findByContent( "重要");
        searchHits.forEach(hit -> {
            // 打印原始文档内容
            System.out.println("Document: " + hit.getContent());

            // 打印高亮字段
            hit.getHighlightFields().forEach((fieldName, highlight) -> {
                System.out.println("Field: " + fieldName);
                // System.out.println("Highlights: " + String.join(", ", highlight.fragments()));
            });
        });

    }

}