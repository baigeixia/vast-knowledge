package com.vk.behaviour.service.impl;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.vk.article.domain.ApArticle;
import com.vk.article.domain.HomeArticleListVo;
import com.vk.article.feign.RemoteClientArticleQueryService;
import com.vk.behaviour.domain.ApReadBehavior;
import com.vk.behaviour.domain.vo.LocalReadSearchVo;
import com.vk.behaviour.domain.vo.ReadDataAnalysisVo;
import com.vk.behaviour.domain.vo.UserFootMarkListVo;
import com.vk.behaviour.mapper.ApReadBehaviorMapper;
import com.vk.behaviour.service.ApReadBehaviorService;
import com.vk.comment.feign.RemoteClientCommentQueryService;
import com.vk.common.core.domain.R;
import com.vk.common.core.domain.ValidationUtils;
import com.vk.common.core.exception.LeadNewsException;
import com.vk.common.core.utils.RequestContextUtil;
import com.vk.common.core.utils.StringUtils;
import com.vk.common.es.domain.UserReadDocument;
import com.vk.common.es.repository.UserReadDocumentRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.HighlightQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.core.query.highlight.Highlight;
import org.springframework.data.elasticsearch.core.query.highlight.HighlightField;
import org.springframework.data.elasticsearch.core.query.highlight.HighlightParameters;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    private RemoteClientCommentQueryService remoteClientCommentQueryService;

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
        Long userId = RequestContextUtil.getUserIdNotLogin();
        if (!StringUtils.isLongEmpty(userId)) {
            QueryWrapper wrapper = QueryWrapper.create().select();
            wrapper.where(AP_READ_BEHAVIOR.ARTICLE_ID.eq(id).and(AP_READ_BEHAVIOR.ENTRY_ID.eq(userId)));
            return mapper.selectOneByQuery(wrapper);
        }
        return null;
    }

    @Override
    public Long selectCount(LocalDateTime now) {
        return mapper.selectCount(now);
    }

    @Override
    @Async("asyncTaskExecutor")
    public void importAll(long page, Long size, CountDownLatch countDownLatch, LocalDateTime now) {
        String threadName = Thread.currentThread().getName();
        log.info("{} start: page={},size={}", threadName, page, size);
        // 1分页查询到数据
        List<UserReadDocument> readDocumentList = mapper.selectByPage((page - 1) * size, size, now);
        getArticleIds(readDocumentList);

        log.info("{} start: page={},size={}, actualSize={} found", threadName, page, size, readDocumentList.size());
        // 2.分批导入到ES中
        try {
            userReadDocumentRepository.saveAll(readDocumentList);
            log.info("{} end: page={},size={}, actualSize={} found", threadName, page, size, readDocumentList.size());
        } catch (Exception e) {
            log.error("{} error: page={},size={}, actualSize={} found", threadName, page, size, readDocumentList.size(), e);
        } finally {
            // 减掉数量
            countDownLatch.countDown();
        }
    }

    private void getArticleIds(List<UserReadDocument> readDocumentList) {
        Set<Long> ids = readDocumentList.stream().map(UserReadDocument::getArticleId).collect(Collectors.toSet());
        if (!ids.isEmpty()) {
            R<Map<Long, String>> title = remoteClientArticleQueryService.getArticleTitle(ids);
            ValidationUtils.validateR(title, "文章服务未知异常");
            Map<Long, String> titleData = title.getData();

            for (UserReadDocument readDocument : readDocumentList) {
                readDocument.setTitle(titleData.get(readDocument.getArticleId()));
            }
        } else {
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
    public LocalReadSearchVo searchRead(String query, Long page, Long size) {
        if (StringUtils.isEmpty(query)) {
            throw new LeadNewsException("搜索内容不能为空");
        }
        LocalReadSearchVo resultVo = new LocalReadSearchVo();
        SearchHits<UserReadDocument> searchHits = EsQueryRead(query, page, size);
        long totalHits = searchHits.getTotalHits();
        resultVo.setTotalHits(totalHits);

        Map<Long, String> highlightMap = new LinkedHashMap<>();
        List<UserReadDocument> readDocuments = searchHits.stream().map(hit -> {
            StringBuilder highlightTitleBuilder = new StringBuilder();
            Long articleId = hit.getContent().getArticleId();
            hit.getHighlightFields().forEach((fieldName, highlight) -> {
                if (highlight != null && !highlight.isEmpty()) {
                    // 合并高亮标题
                    if (!highlightTitleBuilder.isEmpty()) {
                        highlightTitleBuilder.append(", ");
                    }
                    highlightTitleBuilder.append(String.join(", ", highlight));
                }
            });
            highlightMap.merge(articleId, highlightTitleBuilder.toString(), (existing, newTitle) -> existing + ", " + newTitle);
            return hit.getContent();
        }).toList();


        Set<Long> articleIds = readDocuments.stream().map(UserReadDocument::getArticleId).collect(Collectors.toSet());

        List<UserFootMarkListVo> userFootMarkListVos = buildFootMarkList(articleIds, readDocuments, UserReadDocument::getArticleId, UserReadDocument::getUpdatedTime);
        userFootMarkListVos.forEach(i -> i.getFootMark().forEach(h -> h.setTitle(highlightMap.get(h.getId()))));
        resultVo.setFootMarkLists(userFootMarkListVos);

        return resultVo;
    }

    private final ReentrantLock lock = new ReentrantLock();

    @Override
    @Async("asyncTaskExecutor")
    public void clearAll(Long userid) {
        log.info("开始执行 clearAll for userId: {}", userid);

        if (lock.tryLock()) { // 使用 tryLock 防止死锁
            try {
                log.info("执行 clearAll");
                List<UserReadDocument> documents = userReadDocumentRepository.findByEntryId(userid);

                if (!documents.isEmpty()) {
                    userReadDocumentRepository.deleteAll(documents);
                    log.info("成功删除 {} 个文档", documents.size());
                } else {
                    log.info("没有找到要删除的文档");
                }

                mapper.clearAll(userid);
            } catch (Exception e) {
                log.error("清除操作失败", e);
            } finally {
                lock.unlock(); // 确保释放锁
            }
        } else {
            log.warn("无法获取锁，clearAll 操作被跳过");
        }
    }

    @Override
    public ReadDataAnalysisVo readDataAnalysis(
            Long articleId, LocalDateTime startTime, LocalDateTime endTime, Integer cycle) {
        if (StringUtils.isLongEmpty(articleId)) {
            throw new LeadNewsException("文章错误");
        }
        if (Stream.of(0, 1, 2, 3).noneMatch(Predicate.isEqual(cycle))) {
            throw new LeadNewsException("错误的日期");
        }

        if (!ObjectUtils.isEmpty(startTime) && !ObjectUtils.isEmpty(endTime)) {
            return processDataByDateRange(articleId, startTime, endTime);
        }
        LocalDateTime cycleStartTime = null;
        LocalDateTime cycleEndTime = null;

        // 如果 startTime 和 endTime 为空，根据其他时间参数进行处理
        switch (cycle) {
            case 0 -> {
                // 今天
                cycleStartTime = getStartOfDay();
                cycleEndTime = getEndOfDay();
            }
            case 1 -> {
                // 本周
                cycleStartTime = getStartOfWeek();
                cycleEndTime = getEndOfWeek();
            }
            case 2 -> {
                // 整周
                cycleStartTime = getStartOfWholeWeek();
                cycleEndTime = getEndOfWholeWeek();
            }
            case 3 -> {
                // 整月
                cycleStartTime = getStartOfWholeMonth();
                cycleEndTime = getEndOfWholeMonth();
            }
        }

        return processDataByDateRange(articleId, cycleStartTime, cycleEndTime);
    }

    // 统一的日期范围处理方法
    private ReadDataAnalysisVo processDataByDateRange(Long articleId, LocalDateTime start, LocalDateTime end) {
        R<ApArticle> oneArticle = remoteClientArticleQueryService.getOneArticle(articleId);
        ValidationUtils.validateR(oneArticle, "文章错误");
        ApArticle articleData = oneArticle.getData();
        Long userId = RequestContextUtil.getUserId();

        if (!articleData.getAuthorId().equals(userId)) {
            throw new LeadNewsException("权限不足无法查询");
        }

        ReadDataAnalysisVo vo = new ReadDataAnalysisVo();

        // 处理基于时间范围的数据
        vo = mapper.getReadData(articleId, start, end);
        R<Long> commentById = remoteClientCommentQueryService.getArticleCommentById(articleId, start, end);
        if (ValidationUtils.validateRSuccess(commentById)) {
            Long countComment = commentById.getData();
            vo.setComment(countComment);
        }

        vo.setReadAnalysis(mapper.getReadAnalysis(articleId, start, end));

        return vo;
    }

    // 获取当天的开始时间
    private LocalDateTime getStartOfDay() {
        return LocalDateTime.now().toLocalDate().atStartOfDay();
    }

    // 获取当天的结束时间
    private LocalDateTime getEndOfDay() {
        return LocalDateTime.now().toLocalDate().atTime(23, 59, 59, 999999);
    }


    // 获取本周的开始时间（星期一）
    private LocalDateTime getStartOfWeek() {
        return LocalDateTime.now().with(DayOfWeek.MONDAY).toLocalDate().atStartOfDay();
    }

    // 获取本周的结束时间（星期日）
    private LocalDateTime getEndOfWeek() {
        return LocalDateTime.now().with(DayOfWeek.SUNDAY).toLocalDate().atTime(23, 59, 59, 999999);
    }


    // 获取最近7天的开始时间（wholeWeek）
    private LocalDateTime getStartOfWholeWeek() {
        return LocalDateTime.now().minusDays(7).toLocalDate().atStartOfDay();
    }

    // 获取最近7天的结束时间（wholeWeek）
    private LocalDateTime getEndOfWholeWeek() {
        return LocalDateTime.now().toLocalDate().atTime(23, 59, 59, 999999);
    }


    // 获取最近30天的开始时间（wholeMoon）
    private LocalDateTime getStartOfWholeMonth() {
        return LocalDateTime.now().minusDays(30).toLocalDate().atStartOfDay();
    }

    // 获取最近30天的结束时间（wholeMoon）
    private LocalDateTime getEndOfWholeMonth() {
        return LocalDateTime.now().toLocalDate().atTime(23, 59, 59, 999999);
    }


    /**
     * 查询文章返回 List<UserFootMarkListVo>
     *
     * @param ids
     * @param readBehaviors
     * @param articleIdExtractor
     * @param updatedTimeExtractor
     * @param <T>
     * @return
     */
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

    private SearchHits<UserReadDocument> EsQueryRead(String query, Long page, Long size) {
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
                .withPageable(PageRequest.of(page.intValue() - 1, size.intValue()))
                .withSort(Sort.sort(UserReadDocument.class).by(UserReadDocument::getUpdatedTime).descending())
                // 粉丝最多排序  默认
                .build();

        return elasticsearchOperations.search(nativeQueryquery, UserReadDocument.class);
    }


}
