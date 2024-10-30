package com.vk.behaviour;


import com.vk.behaviour.mapper.ApLikesBehaviorMapper;
import com.vk.common.es.domain.UserReadDocument;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.HighlightQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.core.query.highlight.Highlight;
import org.springframework.data.elasticsearch.core.query.highlight.HighlightField;
import org.springframework.data.elasticsearch.core.query.highlight.HighlightParameters;

import java.util.List;

@SpringBootTest(classes = {TestBehaviourApplication.class})
public class TestBehaviourApplication {


    @Autowired
    private ApLikesBehaviorMapper apLikesBehaviorMapper;

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    @Test
    void testLikeN() {
        Long page = 1L;
        Long size = 5L;

        List<String> dataTime = apLikesBehaviorMapper.getTimeRange((page - 1) * size, size);
        System.out.println(dataTime);
    }

    @Test
    void EsQueryRead() {
        String pre = "<span style='color:red'>";
        String post = "</span>";
        String query = "张";
        Long userid = 1L;
        int page = 1;
        int size = 10;

        Query nativeQueryquery = NativeQuery.builder()
                .withQuery(q -> q
                        .bool(b -> b
                                .should(s -> s.match(m -> m.field("title").query(query)))
                                .should(s -> s.term(m -> m.field("entryId").value(userid)))
                        )
                )
                // 指定要高亮的字段将其加上头尾标签
                .withHighlightQuery(
                        new HighlightQuery(
                                new Highlight(
                                        HighlightParameters.builder().withPreTags(pre).withPostTags(post).build(),
                                        List.of(new HighlightField("title"))
                                ), String.class)
                )
                .withPageable(PageRequest.of(page - 1, size))
                // 粉丝最多排序  默认
                .build();

        SearchHits<UserReadDocument> searchHits = elasticsearchOperations.search(nativeQueryquery, UserReadDocument.class, IndexCoordinates.of("user_read", "article"));
        searchHits.forEach(hit -> {
            // 打印原始文档内容
            System.out.println("Document: " + hit.getContent());

            // 打印高亮字段
            hit.getHighlightFields().forEach((fieldName, highlight) -> {
                System.out.println("Field: " + fieldName);
                System.out.println("Highlights: " + String.join(", ", highlight));
            });
        });

    }
}
