package com.vk.behaviour.service.impl;

import co.elastic.clients.json.JsonData;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.vk.article.domain.HomeArticleListVo;
import com.vk.article.feign.RemoteClientArticleQueryService;
import com.vk.behaviour.domain.ApReadBehavior;
import com.vk.behaviour.domain.vo.UserFootMarkListVo;
import com.vk.behaviour.mapper.ApReadBehaviorMapper;
import com.vk.behaviour.service.ApReadBehaviorService;
import com.vk.common.core.domain.R;
import com.vk.common.core.domain.ValidationUtils;
import com.vk.common.core.exception.LeadNewsException;
import com.vk.common.core.utils.RequestContextUtil;
import com.vk.common.core.utils.SpringUtils;
import com.vk.common.core.utils.StringUtils;
import com.vk.common.es.domain.UserInfoDocument;
import com.vk.common.es.domain.UserReadDocument;
import com.vk.common.es.repository.UserReadDocumentRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.HighlightQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.core.query.highlight.Highlight;
import org.springframework.data.elasticsearch.core.query.highlight.HighlightField;
import org.springframework.data.elasticsearch.core.query.highlight.HighlightParameters;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.vk.behaviour.domain.table.ApReadBehaviorTableDef.AP_READ_BEHAVIOR;

/**
 * APP阅读行为 服务层实现。
 *
 * @author 张三
 * @since 2024-05-13
 */
@Service
@Slf4j
public class ApReadBehaviorServiceImpl extends ServiceImpl<ApReadBehaviorMapper, ApReadBehavior> implements ApReadBehaviorService {

    @Autowired
    private RemoteClientArticleQueryService remoteClientArticleQueryService;

    @Autowired
    private UserReadDocumentRepository userReadDocumentRepository;

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    @Override
    public List<UserFootMarkListVo> getUserFootMark(Long page, Long size) {
        List<UserFootMarkListVo> vos = new ArrayList<>();
        Long userId = RequestContextUtil.getUserId();
        page = (page - 1) * size;
        List<ApReadBehavior> readBehaviors = mapper.selectListUserRead(userId, page, size);
        Set<Long> ids = readBehaviors.stream().map(ApReadBehavior::getArticleId).collect(Collectors.toSet());
         vos = buildFootMarkList(ids, readBehaviors, ApReadBehavior::getArticleId, ApReadBehavior::getUpdatedTime);
        // if (!ObjectUtils.isEmpty(ids)) {
        //     R<Map<Long, HomeArticleListVo>> idList = remoteClientArticleQueryService.getArticleIdList(ids);
        //     ValidationUtils.validateR(idList, "文章查询失败");
        //     Map<Long, HomeArticleListVo> idListData = idList.getData();
        //     vos = readBehaviors.stream()
        //             .collect(Collectors.groupingBy( ApReadBehavior::getUpdatedTime,
        //                     Collectors.mapping( data->
        //                                 idListData.get(data.getArticleId()),
        //                             Collectors.toList()
        //                     )
        //             ))
        //             .entrySet().stream()
        //             .map(entry -> {
        //                 UserFootMarkListVo  listVo = new UserFootMarkListVo();
        //                 listVo.setFootMark(entry.getValue());
        //                 listVo.setDateTime(entry.getKey());
        //                 return listVo;
        //             })
        //             .sorted(Comparator.comparing(UserFootMarkListVo::getDateTime).reversed())  // Sort by statisticsTime in descending order
        //             .toList();
        // }

        return vos;
    }

    @Override
    public ApReadBehavior getArticleInfo(Long id) {
        Long userId = RequestContextUtil.getUserId();
        QueryWrapper wrapper = QueryWrapper.create().select();
        wrapper.where(AP_READ_BEHAVIOR.ARTICLE_ID.eq(id).and(AP_READ_BEHAVIOR.ENTRY_ID.eq(userId)));
        return mapper.selectOneByQuery(wrapper);
    }

    @Override
    public Long selectCount(LocalDateTime now) {
        return mapper.selectCount(now);
    }

    @Override
    @Async("asyncTaskExecutor")
    public void importAll(long page, Long size, CountDownLatch countDownLatch, LocalDateTime now) {
        String threadName = Thread.currentThread().getName();
        log.info("{} start: page={},size={}", threadName,page,size);
        //1分页查询到数据
        List<UserReadDocument> readDocumentList = mapper.selectByPage((page - 1) * size, size,now);
        getArticleIds(readDocumentList);

        log.info("{} start: page={},size={}, actualSize={} found", threadName,page,size, readDocumentList.size());
        //2.分批导入到ES中
        try {
            userReadDocumentRepository.saveAll(readDocumentList);
            log.info("{} end: page={},size={}, actualSize={} found", threadName,page,size, readDocumentList.size());
        } catch (Exception e) {
            log.error("{} error: page={},size={}, actualSize={} found", threadName,page,size, readDocumentList.size(),e);
        } finally {
            //减掉数量
            countDownLatch.countDown();
        }
    }

    private void getArticleIds(List<UserReadDocument> readDocumentList) {
        Set<Long> ids = readDocumentList.stream().map(UserReadDocument::getArticleId).collect(Collectors.toSet());
        if (!ids.isEmpty()){
            R<Map<Long, String>> title = remoteClientArticleQueryService.getArticleTitle(ids);
            ValidationUtils.validateR(title, "文章服务未知异常");
            Map<Long, String> titleData = title.getData();

            for (UserReadDocument readDocument : readDocumentList) {
                readDocument.setTitle(titleData.get(readDocument.getArticleId()));
            }
        }else {
            log.error("readDocumentList 为空 ");
        }
    }

    @Override
    public List<UserReadDocument> selectForCondition(LocalDateTime redisTime, LocalDateTime now) {
        List<UserReadDocument> userReadDocuments = mapper.selectForCondition(redisTime, now);
        getArticleIds(userReadDocuments);
        return userReadDocuments;
    }

    @Override
    public List<UserFootMarkListVo> searchRead(String query, Long page, Long size) {
        if (StringUtils.isEmpty(query)){
            throw  new LeadNewsException("搜索内容不能为空");
        }

        SearchHits<UserReadDocument> searchHits = EsQueryRead(query, page, size);
        long totalHits = searchHits.getTotalHits();

        List<UserReadDocument> readDocuments = searchHits.stream().map(SearchHit::getContent).toList();

        Set<Long> articleIds = readDocuments.stream().map(UserReadDocument::getArticleId).collect(Collectors.toSet());

        return buildFootMarkList(articleIds, readDocuments, UserReadDocument::getArticleId, UserReadDocument::getUpdatedTime);
    }

    private <T> List<UserFootMarkListVo> buildFootMarkList(Set<Long> ids, List<T> readBehaviors, Function<T, Long> articleIdExtractor, Function<T, LocalDateTime> updatedTimeExtractor) {
        if (ObjectUtils.isEmpty(ids)) {
            return Collections.emptyList(); // 如果 ids 为空，返回空列表
        }

        R<Map<Long, HomeArticleListVo>> idListResponse = remoteClientArticleQueryService.getArticleIdList(ids);
        ValidationUtils.validateR(idListResponse, "文章查询失败");
        Map<Long, HomeArticleListVo> idListData = idListResponse.getData();

        return readBehaviors.stream()
                .collect(Collectors.groupingBy(updatedTimeExtractor,
                        Collectors.mapping(data -> idListData.get(articleIdExtractor.apply(data)), Collectors.toList())
                ))
                .entrySet().stream()
                .map(entry -> {
                    UserFootMarkListVo listVo = new UserFootMarkListVo();
                    listVo.setFootMark(entry.getValue());
                    listVo.setDateTime(entry.getKey());
                    return listVo;
                })
                .sorted(Comparator.comparing(UserFootMarkListVo::getDateTime).reversed())
                .toList();
    }

    private  SearchHits<UserReadDocument> EsQueryRead(String query, Long page, Long size) {
        String pre = "<span style='color:red'>";
        String post = "</span>";
        Long localUserId = RequestContextUtil.getUserId();
        Query nativeQueryquery = NativeQuery.builder()
                .withQuery(q -> q
                        .bool(b -> b
                                .must(s -> s.match(m -> m.field("title").query(query)))
                                .must(s -> s.term(m -> m.field("entryId").value(localUserId)))
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
                .withPageable(PageRequest.of(page.intValue() - 1 , size.intValue()))
                .withSort(Sort.sort(UserReadDocument.class).by(UserReadDocument::getUpdatedTime).descending())
                //粉丝最多排序  默认
                .build();

        return elasticsearchOperations.search(nativeQueryquery, UserReadDocument.class);
    }


}
