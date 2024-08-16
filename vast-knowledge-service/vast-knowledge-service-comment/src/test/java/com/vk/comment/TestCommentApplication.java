package com.vk.comment;

import com.vk.comment.document.ApCommentDocument;
import com.vk.comment.domain.dto.CommentSaveDto;
import com.vk.comment.repository.DocumentRepository;
import com.vk.comment.service.ApCommentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.Query;

import java.util.Date;


@SpringBootTest
public class TestCommentApplication {

    @Autowired
    private ApCommentService apCommentService;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

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

        // SearchHits<ApCommentDocument> searchHits = documentRepository.findByEntryIdAndId(24L, 42696601722000119L, "重要");
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

        SearchHits<ApCommentDocument> searchHits = documentRepository.findByContent( "重要");
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