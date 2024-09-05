package com.vk.comment;

import com.alibaba.fastjson2.JSON;
import com.mybatisflex.core.query.QueryWrapper;
import com.vk.comment.document.ApCommentDocument;
import com.vk.comment.document.ApCommentRepayDocument;
import com.vk.comment.document.NotificationDocument;
import com.vk.comment.domain.ApComment;
import com.vk.comment.domain.ApCommentRepay;
import com.vk.comment.domain.table.ApCommentTableDef;
import com.vk.comment.repository.CommentDocumentRepository;
import com.vk.comment.repository.CommentRepayDocumentRepository;
import com.vk.comment.service.ApCommentRepayService;
import com.vk.comment.service.ApCommentService;
import com.vk.common.core.constant.DatabaseConstants;
import com.vk.common.core.utils.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ReactiveElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;

import static com.vk.comment.domain.table.ApCommentRepayTableDef.AP_COMMENT_REPAY;


@SpringBootTest
public class TestCommentApplication {

    @Autowired
    private ApCommentService apCommentService;
    @Autowired
    private ApCommentRepayService apCommentRepayService;

    @Autowired
    private ApCommentRepayService commentRepayService;

    @Autowired
    private CommentDocumentRepository commentDocumentRepository;

    @Autowired
    private CommentRepayDocumentRepository commentRepayDocumentRepository;


    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    @Autowired
    private ReactiveElasticsearchOperations operations;



    @Test
    void testUpRepayEntryId() {
        List<ApCommentRepay> list = apCommentRepayService.list();
        for (ApCommentRepay commentRepay : list) {
            Long entryId = commentRepay.getEntryId();
            if (StringUtils.isLongEmpty(entryId)){
                Long commentId = commentRepay.getCommentId();
                ApComment byId = apCommentService.getById(commentId);
                if (null!=byId){
                    Long byIdEntryId = byId.getEntryId();
                    ApCommentRepay repay = new ApCommentRepay();
                    repay.setId(commentRepay.getId());
                    repay.setEntryId(byIdEntryId);
                    apCommentRepayService.updateById(repay);
                }
            }
        }

    }

    @Test
    void testReactiveOperations() {
        int authorId = 1;
        Query arAuthorId = new CriteriaQuery(new Criteria("arAuthorId").is(authorId).or("id").is(42695511698000118L));
        operations.search(arAuthorId, NotificationDocument.class, IndexCoordinates.of("comment", "comment_repay")).doOnNext(System.out::println).onErrorResume(e -> {
            System.err.println("Error occurred: " + e.getMessage());
            return Mono.empty(); // Handle the error and return an empty result
        }).subscribe();
    }

    @Test
    void testOperations() {
        // .withSort(Sort.by(Sort.Order.desc("createdTime")))

        List<Long> entryIdList = List.of(16L, 24L, 28L); // Example list of entry IDs
        int authorId = 2;
        // Query query = NativeQuery.builder()
        //         .withQuery( q->q.regexp(ma -> ma))
        //         .withPageable(PageRequest.of(2, 10))
        //         .withSort(Sort.sort(NotificationDocument.class).by(NotificationDocument::getCreatedTime).descending())
        //         .build();

        // Query query = NativeQuery.builder()
        //         .withQuery( q->q.bool(ma -> ma))
        //         .withPageable(PageRequest.of(2, 10)) // 分页
        //         .withSort(Sort.by(Sort.Order.desc("createdTime"))) // 排序
        //         .build();

        Query arAuthorId = new CriteriaQuery(new Criteria("arAuthorId").is(authorId).or("id").is(42695511698000118L));
        // .withAggregation("lastNames", Aggregation.of(a -> a
        //       .terms(ta -> ta.field("lastName").size(10))))
        // Query query = NativeQuery.builder()
        //         .withQuery(q -> q
        //                 .bool(b ->b
        //                         .should(s->s
        //                                 .match(m-> m.field("arAuthorId").query(authorId))
        //                         )
        //                         .should(s->s.
        //                                 nested(n->n.
        //                                         path("comment_repay")
        //                                         .query(nq -> nq
        //                                                 .bool(bn -> bn
        //                                                         .must(m -> m
        //                                                                 .match(mq -> mq
        //                                                                         .field("comment_repay.commentRepayId")
        //                                                                         .query("comment.id")
        //                                                                 )
        //                                                         )
        //                                                         .must(m -> m
        //                                                                 .match(mq -> mq
        //                                                                         .field("comment.authorId")
        //                                                                         .query(authorId)
        //                                                                 )
        //                                                         )
        //                                                 )
        //                                         )
        //                                 )
        //                         )
        //                 )
        //         )
        //         .withPageable(PageRequest.of(0, 10))
        //         .withSort(Sort.sort(NotificationDocument.class).by(NotificationDocument::getCreatedTime).descending())
        //         .build();
        Query query = NativeQuery.builder()
                .withQuery(q -> q
                        .bool(b -> b
                                .should(s -> s.term(m -> m.field("arAuthorId").value(authorId)))
                                .should(s -> s.term(m -> m.field("repayAuthorId").value(authorId)))
                                .mustNot(m -> m.term(t -> t.field("authorId").value(authorId)))
                                .minimumShouldMatch("1")
                        )
                )
                .withPageable(PageRequest.of(1, 10))
                .withSort(Sort.sort(NotificationDocument.class).by(NotificationDocument::getCreatedTime).descending())
                .build();
        // elasticsearchOperations.save()
        // operations.search(arAuthorId, NotificationDocument.class,IndexCoordinates.of("comment"))
        //         .doOnNext(System.out::println).subscribe();

        // Criteria criteria = new Criteria("arAuthorId").is(authorId)
        //         .subCriteria(
        //                 new Criteria().or("commentRepayId").is(
        //                         new Criteria("arAuthorId").is(authorId)
        //                 )
        //         );
        //
        // Query query = new CriteriaQuery(criteria);
        // query.setPageable(PageRequest.of(0, 10)).addSort(Sort.sort(NotificationDocument.class).by(NotificationDocument::getCreatedTime).descending());

        // Query simpleQuery = NativeQuery.builder()
        //         .withQuery(q -> q
        //                 .match(m -> m.field("arAuthorId").query(authorId))
        //         )
        //         .build();

        // Query simpleQuery = NativeQuery.builder()
        //         .withQuery(q -> q
        //                 .bool(b -> b
        //                         .must(m -> m
        //                                 .matchAll(mm -> mm)  // 匹配 ap_comment_repay 表中的所有记录
        //                         )
        //                         .should(sh -> sh
        //                                 .bool(b1 -> b1
        //                                         .must(m1 -> m1
        //                                                 .term(t -> t
        //                                                         .field("comment_repay_id")  // 连接条件1
        //                                                         .value(1)  // 用于 ap_comment 表中 author_id = 1 的 ID
        //                                                 )
        //                                         )
        //                                         .must(m2 -> m2
        //                                                 .term(t -> t
        //                                                         .field("author_id")  // 限制 author_id 为 1
        //                                                         .value(1)
        //                                                 )
        //                                         )
        //                                 )
        //                         )
        //                         .should(sh -> sh
        //                                 .bool(b2 -> b2
        //                                         .must(m3 -> m3
        //                                                 .term(t -> t
        //                                                         .field("comment_repay_id")  // 连接条件2
        //                                                         .value(1)  // 用于 ap_comment_repay 表中 author_id = 1 的 ID
        //                                                 )
        //                                         )
        //                                         .must(m4 -> m4
        //                                                 .term(t -> t
        //                                                         .field("author_id")  // 限制 author_id 为 1
        //                                                         .value(1)
        //                                                 )
        //                                         )
        //                                 )
        //                         )
        //                 )
        //         ).build();

        SearchHits<NotificationDocument> search = elasticsearchOperations.search(query, NotificationDocument.class, IndexCoordinates.of("comment", "comment_repay"));
        // SearchHits<NotificationDocument> search = elasticsearchOperations.search(arAuthorId, NotificationDocument.class, IndexCoordinates.of("comment"));
        List<NotificationDocument> notificationDocuments = search.stream().map(SearchHit::getContent).toList();

        Map<String, List<NotificationDocument>> groupedMap = notificationDocuments.stream()
                .collect(Collectors.groupingBy(doc -> doc.getCreatedTime().toLocalDate().toString()));

        // 对 groupedMap 进行排序：按键降序排序
        Map<String, List<NotificationDocument>> sortedMap = groupedMap.entrySet()
                .stream()
                .sorted(Map.Entry.<String, List<NotificationDocument>>comparingByKey(Comparator.reverseOrder())) // 降序排序
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new // 使用 LinkedHashMap 保持排序顺序
                ));




        String jsonString = JSON.toJSONString(sortedMap);
        System.out.println(jsonString);
    }

    @Test
    void testRepaySaveAll() {
        List<ApCommentRepayDocument> apCommentRepayDocuments = commentRepayService
                .listAs(QueryWrapper.create().select().where(AP_COMMENT_REPAY.STATUS.eq(DatabaseConstants.DB_ROW_STATUS_YES)), ApCommentRepayDocument.class);

        apCommentRepayDocuments.forEach(System.out::println);
        commentRepayDocumentRepository.saveAll(apCommentRepayDocuments);
    }

    @Test
    void testComSaveAll() {
        List<ApCommentDocument> apCommentDocuments = apCommentService.listAs(QueryWrapper.create().select().where(ApCommentTableDef.AP_COMMENT.STATUS.eq(DatabaseConstants.DB_ROW_STATUS_YES)), ApCommentDocument.class);
        try {
            commentDocumentRepository.saveAll(apCommentDocuments);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testRepayGet() {
        List<Long> ids = List.of(44327337076000185L);
        Iterable<ApCommentRepayDocument> allById = commentRepayDocumentRepository.findAllById(ids);
        allById.forEach(System.out::println);
    }


    @Test
    void testEsGetInfo() {
        Integer[] arId = {16, 24, 28};
        int page = 0; // 当前页码
        int size = 10; // 每页大小
        // Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("createdTime")));
        Pageable pageable = PageRequest.of(page, size);
        List<ApCommentDocument> documents = commentDocumentRepository.findAllByEntryIdInOrderByCreatedTimeDesc(arId, pageable);
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
    void testComment() {
        long i = 1722583380009L;
        long time = new Date().getTime();
        System.out.println(time - i);
        // CommentSaveDto saveDto = new CommentSaveDto();
        // saveDto.setContent("");
        // apCommentService.saveComment(saveDto);
    }


    @Test
    void testComment1() {

        // ApCommentDocument commentDocument = elasticsearchOperations.get("42696889904000157", ApCommentDocument.class);
        // System.out.println(commentDocument);
        Criteria criteria = new Criteria("entryId").is(16)
                .subCriteria(
                        new Criteria("id").is(1)
                                .or("id").is(6)
                );

        SearchHits<ApCommentDocument> search = elasticsearchOperations.search(new CriteriaQuery(criteria), ApCommentDocument.class);
        search.forEach(System.out::println);

    }

    @Test
    void testComment2() {

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

        SearchHits<ApCommentDocument> searchHits = commentDocumentRepository.findByContent("重要");
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