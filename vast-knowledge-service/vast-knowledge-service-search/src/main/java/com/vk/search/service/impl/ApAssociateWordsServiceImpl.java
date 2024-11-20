package com.vk.search.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.vk.common.core.utils.StringUtils;
import com.vk.common.es.domain.AssociateWordsDocument;
import com.vk.common.es.repository.AssociateWordsDocumentRepository;
import com.vk.search.domain.ApAssociateWords;
import com.vk.search.domain.vo.AssociateListVo;
import com.vk.search.mapper.ApAssociateWordsMapper;
import com.vk.search.service.ApAssociateWordsService;
import lombok.extern.slf4j.Slf4j;
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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * 联想词 服务层实现。
 *
 * @author 张三
 * @since 2024-05-13
 */
@Service
@Slf4j
public class ApAssociateWordsServiceImpl extends ServiceImpl<ApAssociateWordsMapper, ApAssociateWords> implements ApAssociateWordsService {

    @Autowired
    private AssociateWordsDocumentRepository associateWordsDocumentRepository;

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    @Override
    public List<AssociateListVo> getESList(String text) {
        List<AssociateListVo> associates=new ArrayList<>();
        if (StringUtils.isNotEmpty(text)){
            String lowerCase = text.trim().toLowerCase();
            associates = EsQueryAssociate(lowerCase);
        }
        return associates;
    }

    @Override
    public void importAll(long page, Long size, CountDownLatch countDownLatch, LocalDateTime now) {
        String threadName = Thread.currentThread().getName();
        log.info("{} start: page={},size={}", threadName,page,size);
        //1分页查询到数据
        List<AssociateWordsDocument> associateWordsDocuments = mapper.selectByPage((page - 1) * size, size,now);
        log.info("{} start: page={},size={}, actualSize={} found", threadName,page,size, associateWordsDocuments.size());
        //2.分批导入到ES中
        try {
            associateWordsDocumentRepository.saveAll(associateWordsDocuments);
            log.info("{} end: page={},size={}, actualSize={} found", threadName,page,size, associateWordsDocuments.size());
        } catch (Exception e) {
            log.error("{} error: page={},size={}, actualSize={} found", threadName,page,size, associateWordsDocuments.size(),e);
        } finally {
            //减掉数量
            countDownLatch.countDown();
        }
    }

    private List<AssociateListVo> EsQueryAssociate(String textLowerCase){
        // String pre = "<span style='color:#969799'>";
        String pre = "<span style='color:red'>";
        String post = "</span>";
        //查询字段
        String field = "associateWords";

        Query nativeQueryquery = NativeQuery.builder()
                .withQuery(q -> q
                        .bool(b -> {
                            b.should(s -> s.match(m -> m.field(field).query(textLowerCase)));
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
                .withPageable(PageRequest.of(0, 10))
                .withSort(Sort.sort(AssociateWordsDocument.class).by(AssociateWordsDocument::getCreatedTime).descending())
                .withSourceFilter(new FetchSourceFilter(new String[]{field},null))  // 控制显示的字段
                .build();

        SearchHits<AssociateWordsDocument> search = elasticsearchOperations.search(nativeQueryquery, AssociateWordsDocument.class);

        List<AssociateListVo> highlightS=new ArrayList<>();

        search.forEach(hit -> {
            AssociateListVo listVo = new AssociateListVo();
            String associateWords = hit.getContent().getAssociateWords();
            listVo.setAssociateWords(associateWords);
            hit.getHighlightFields().forEach((fieldName, highlight) -> {
                String associateWordH = String.join(", ", highlight);
                listVo.setAssociateWordH(associateWordH);
                highlightS.add(listVo);
            });
        });

        return highlightS;

    }
}
