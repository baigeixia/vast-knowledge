package com.vk.search.service.impl;

import co.elastic.clients.json.JsonData;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.vk.article.domain.HomeArticleListVo;
import com.vk.article.feign.RemoteClientArticleQueryService;
import com.vk.common.core.domain.R;
import com.vk.common.core.domain.ValidationUtils;
import com.vk.common.core.exception.LeadNewsException;
import com.vk.common.es.domain.ArticleInfoDocument;
import com.vk.search.domain.ApUserSearch;
import com.vk.search.mapper.ApUserSearchMapper;
import com.vk.search.service.ApUserSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.HighlightQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.core.query.highlight.Highlight;
import org.springframework.data.elasticsearch.core.query.highlight.HighlightField;
import org.springframework.data.elasticsearch.core.query.highlight.HighlightParameters;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * APP用户搜索信息 服务层实现。
 *
 * @author 张三
 * @since 2024-05-13
 */
@Service
public class ApUserSearchServiceImpl extends ServiceImpl<ApUserSearchMapper, ApUserSearch> implements ApUserSearchService {

    @Autowired
    private RemoteClientArticleQueryService remoteClientArticleQueryService;

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    /**
     * @param query  搜索内容
     * @param type   头部标题 0 综合     1 文章  3 标签 4 用户
     * @param sort   排序    0 综合排序  1 最新  2 最热
     * @param period 时间    1 不限      2 最新一天  3 最近一周  4最近一月
     * @param page   页数
     * @param size   长度
     * @return
     */
    @Override
    public List<HomeArticleListVo> searchInfo(String query, Integer type, Integer sort, Integer period, Long page, Long size) {
        page = (page - 1);

        if (type == 1 || type == 0) {
            return EsQueryTitle(query, sort, period, page, size);
        }

        return null;
    }

    private List<HomeArticleListVo> EsQueryTitle(String query, Integer sort, Integer period, Long page, Long size) {
        String pre = "<span style='color:red'>";
        String post = "</span>";
        String field = "title";
        // 获取当前时间
        LocalDateTime now = LocalDateTime.now();

        Query nativeQueryquery = NativeQuery.builder()
                .withQuery(q -> q
                        .bool(b -> {
                            b.should(s -> s.matchPhrase(m -> m.field(field).query(query)));
                            //时间排序控制
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
                // 指定要高亮的字段将其加上头尾标签
                .withHighlightQuery(
                        new HighlightQuery(
                                new Highlight(
                                        HighlightParameters.builder().withPreTags(pre).withPostTags(post).build(),
                                        List.of(new HighlightField(field))
                                ), String.class)
                )
                .withPageable(PageRequest.of(page.intValue(), size.intValue()))
                .withSort(Sort.sort(ArticleInfoDocument.class).by(ArticleInfoDocument::getLikes).descending())
                .withSourceFilter(new FetchSourceFilter(new String[]{"title", "id"}, null))  // 控制显示的字段
                .build();

        //排序控制
        if (sort == 0) {
            nativeQueryquery.addSort(
                    Sort.sort(ArticleInfoDocument.class).by(ArticleInfoDocument::getLikes).descending()
                            .and(
                                    Sort.sort(ArticleInfoDocument.class).by(ArticleInfoDocument::getCreatedTime).descending()
                            )
            );
        } else if (sort == 1) {
            nativeQueryquery.addSort(Sort.sort(ArticleInfoDocument.class).by(ArticleInfoDocument::getCreatedTime).descending());
        } else if (sort == 2) {
            nativeQueryquery.addSort(Sort.sort(ArticleInfoDocument.class).by(ArticleInfoDocument::getLikes).descending());
        } else {
            throw new LeadNewsException("错误的排序");
        }


        SearchHits<ArticleInfoDocument> search = elasticsearchOperations.search(nativeQueryquery, ArticleInfoDocument.class);

        //添加高光  文章id 与 高光标题  转map 做替换准备
        Map<Long, String> highlightMap = new HashMap<>();
        search.forEach(hit -> {
            Long id = hit.getContent().getId();
            hit.getHighlightFields().forEach((fieldName, highlight) -> {
                String highlightTitle = String.join(", ", highlight);
                highlightMap.put(id, highlightTitle);
            });
        });

        Set<Long> ids = highlightMap.keySet();
        //ids查询对应文章数据
        R<Map<Long, HomeArticleListVo>> idList = remoteClientArticleQueryService.getArticleIdList(ids);
        ValidationUtils.validateR(idList, "文章查询失败");
        Map<Long, HomeArticleListVo> longHomeArticleListVoMap = idList.getData();

        return longHomeArticleListVoMap.values().stream()
                .peek(i -> i.setTitle(highlightMap.get(i.getId())))
                .toList();
    }
}
