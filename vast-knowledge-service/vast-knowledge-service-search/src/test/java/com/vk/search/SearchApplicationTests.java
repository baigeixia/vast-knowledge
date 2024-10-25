package com.vk.search;

import co.elastic.clients.json.JsonData;
import com.vk.article.domain.HomeArticleListVo;
import com.vk.article.feign.RemoteClientArticleQueryService;
import com.vk.common.core.domain.R;
import com.vk.common.core.domain.ValidationUtils;
import com.vk.common.core.exception.LeadNewsException;
import com.vk.common.es.domain.ArticleInfoDocument;
import com.vk.common.es.repository.ArticleDocumentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.ReactiveElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ReactiveElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.HighlightQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.core.query.highlight.Highlight;
import org.springframework.data.elasticsearch.core.query.highlight.HighlightField;
import org.springframework.data.elasticsearch.core.query.highlight.HighlightParameters;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SpringBootTest
public class SearchApplicationTests {

    @Autowired
    private ArticleDocumentRepository articleDocumentRepository;

    @Autowired
    private ReactiveElasticsearchTemplate reactiveElasticsearchTemplate;
    @Autowired
    private ReactiveElasticsearchOperations reactiveElasticsearchOperations;

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;
    @Autowired
    private RemoteClientArticleQueryService remoteClientArticleQueryService;

    @Test
    void testSearchContent() {
        SearchHits<ArticleInfoDocument> searchInfo = articleDocumentRepository.findByTitle("是");
        searchInfo.forEach(hit -> {
            // 打印原始文档内容
            System.out.println("Document: " + hit.getContent());

            // 打印高亮字段
            hit.getHighlightFields().forEach((fieldName, highlight) -> {
                System.out.println("Field: " + fieldName);
                System.out.println("Highlights: " + String.join(", ", highlight));
            });
        });
    }

    /**
     * @param query  搜索内容
     * @param type   头部标题 0 综合     1 文章  3 标签 4 用户
     * @param sort   排序    0 综合排序  1 最新  2 最热
     * @param period 时间    1 不限      2 最新一天  3 最近一周  4最近一月
     * @param page   页数
     * @param size   长度
     * @return
     */
    @Test
    void testSearchRest() {
        var page = 1;
        var size = 10;
        var type = 1;
        var sort = 2;
        var period = 2;
        String fieldValue = "是";
        if (type == 1) {
            String pre = "<span style='color:red'>";
            String post = "</span>";
            String field = "title";

            // Instant now = Instant.now();
            // Instant oneDayAgo = now.minus(1, ChronoUnit.DAYS);
            // Instant oneWeekAgo = now.minus(1, ChronoUnit.WEEKS);
            // Instant oneMonthAgo = now.minus(1, ChronoUnit.MONTHS);


            // 获取当前时间
            LocalDateTime now = LocalDateTime.now();

            // 计算时间范围
            // LocalDateTime oneDayAgo = now.minusDays(1);
            // LocalDateTime oneWeekAgo = now.minusWeeks(1);
            // LocalDateTime oneMonthAgo = now.minusMonths(1);
            //
            // System.out.println("oneDayAgo：" + oneDayAgo);
            // System.out.println("oneWeekAgo：" + oneWeekAgo);
            // System.out.println("oneMonthAgo：" + oneMonthAgo);
            // System.out.println("now" + now);

            // 转换为时间戳（ISO 8601 格式）

            // String oneDayAgoStr = oneDayAgo.atZone(ZoneId.systemDefault()).toInstant().toString();
            // String oneWeekAgoStr = oneWeekAgo.atZone(ZoneId.systemDefault()).toInstant().toString();
            // String oneMonthAgoStr = oneMonthAgo.atZone(ZoneId.systemDefault()).toInstant().toString();


            // 指定要高亮的字段将其加上头尾标签
            Query query = NativeQuery.builder()
                    .withQuery(q -> q
                            .bool(b -> {
                                b.should(s -> s.matchPhrase(m -> m.field(field).query(fieldValue)));
                                // 添加时间范围条件
                                switch (period) {
                                    case 1:
                                        break;
                                    case 2:
                                        String oneDayAgoStr = now.minusDays(1).atZone(ZoneId.systemDefault()).toInstant().toString();
                                        b.filter(f -> f.range(r -> r.field("createdTime").gte(JsonData.of(oneDayAgoStr))));
                                        break;
                                    case 3:
                                        String oneWeekAgoStr = now.minusWeeks(1).atZone(ZoneId.systemDefault()).toInstant().toString();
                                        b.filter(f -> f.range(r -> r.field("createdTime").gte(JsonData.of(oneWeekAgoStr))));
                                        break;
                                    case 4:
                                        String oneMonthAgoStr = now.minusMonths(1).atZone(ZoneId.systemDefault()).toInstant().toString();
                                        b.filter(f -> f.range(r -> r.field("createdTime").gte(JsonData.of(oneMonthAgoStr))));
                                        break;
                                    default:
                                        throw new LeadNewsException("错误的时间排序");

                                }
                                return b;
                            })
                    )
                    .withHighlightQuery(
                            new HighlightQuery(
                                    new Highlight(
                                            HighlightParameters.builder().withPreTags(pre).withPostTags(post).build(),
                                            List.of(new HighlightField(field))
                                    ), String.class)
                    )
                    .withPageable(PageRequest.of(page - 1, size))
                    .withSort(Sort.sort(ArticleInfoDocument.class).by(ArticleInfoDocument::getLikes).descending())
                    .withSourceFilter(new FetchSourceFilter(new String[]{"title", "id"}, null))  // 控制显示的字段
                    .build();

            if (sort == 0) {
                query.addSort(Sort.sort(ArticleInfoDocument.class).by(ArticleInfoDocument::getLikes).descending().and(Sort.sort(ArticleInfoDocument.class).by(ArticleInfoDocument::getCreatedTime).descending()));
            } else if (sort == 1) {
                query.addSort(Sort.sort(ArticleInfoDocument.class).by(ArticleInfoDocument::getCreatedTime).descending());
            } else if (sort == 2) {
                query.addSort(Sort.sort(ArticleInfoDocument.class).by(ArticleInfoDocument::getLikes).descending());
            } else {
                throw new LeadNewsException("错误的排序");
            }


            SearchHits<ArticleInfoDocument> search = elasticsearchOperations.search(query, ArticleInfoDocument.class);

            // List<ArticleInfoDocument> list = search.stream().map(SearchHit::getContent).toList();

            Map<Long, String> highlightMap = new HashMap<>();
            search.forEach(hit -> {
                // 打印原始文档内容
                System.out.println("Document: " + hit.getContent());
                Long id = hit.getContent().getId();
                // 打印高亮字段
                hit.getHighlightFields().forEach((fieldName, highlight) -> {
                    System.out.println("Field: " + fieldName);
                    System.out.println("Highlights: " + String.join(", ", highlight));
                    String highlightTitle = String.join(", ", highlight);
                    highlightMap.put(id, highlightTitle);
                });
            });

            Set<Long> ids = highlightMap.keySet();
            R<Map<Long, HomeArticleListVo>> idList = remoteClientArticleQueryService.getArticleIdList(ids);
            ValidationUtils.validateR(idList, "文章查询失败");
            Map<Long, HomeArticleListVo> longHomeArticleListVoMap = idList.getData();
            longHomeArticleListVoMap.values().forEach(i -> i.setTitle(highlightMap.get(i.getId())));
            System.out.println("longHomeArticleListVoMap :" + longHomeArticleListVoMap);
        }


    }

    @Test
    void testSearchReactive() {

    }

}