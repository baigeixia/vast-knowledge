package com.vk.article.jobhandler;

import com.vk.article.domain.ArticleInfoDocument;
import com.vk.article.mapper.ApArticleMapper;
import com.vk.article.repository.ArticleDocumentRepository;
import com.vk.article.service.ApArticleService;
import com.vk.article.util.SyncUtil;
import com.vk.common.core.utils.StringUtils;
import com.vk.common.redis.service.RedisService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j(topic="EsArticleSynChXxlJob")
public class EsArticleSynChXxlJob {

    @Autowired
    private RedisService redisService;

    @Autowired
    private ApArticleMapper apArticleMapper;

    @Autowired
    private ArticleDocumentRepository articleDocumentRepository;
    /**
     * 1、简单任务示例（Bean模式）
     */
    @XxlJob("EsArticleSync")
    public void demoJobHandler() {
        String recordTime = redisService.getCacheObject("recordTime");
        if (StringUtils.isEmpty(recordTime)) {
            log.info("执行定时任务taskExecutor: 全量同步未执行,此次不执行");
            XxlJobHelper.log("执行定时任务taskExecutor: 全量同步未执行,此次不执行");
            return;
        }
        if(SyncUtil.sync_status){
            log.info("执行定时任务taskExecutor: 全量同步已执行,但未完成，此次不执行");
            XxlJobHelper.log("执行定时任务taskExecutor: 全量同步已执行,但未完成，此次不执行");
            return;
        }
        log.info("执行定时任务taskExecutor:: 执行时间" + LocalDateTime.now());
        XxlJobHelper.log("执行定时任务taskExecutor:: 执行时间" + LocalDateTime.now());
        //格式化数据
        // LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault());
        LocalDateTime now = LocalDateTime.now();
        redisService.setCacheObject("recordTime",now.format(SyncUtil.FORMATTER_YMDHMS));
        LocalDateTime redisTime = LocalDateTime.parse(recordTime, SyncUtil.FORMATTER_YMDHMS);
        List<ArticleInfoDocument> apArticles = apArticleMapper.selectForCondition(redisTime, now);
        log.info("执行定时任务taskExecutor:: 需要同步的文章size={}, from={},to={}", apArticles==null?0:apArticles.size(),redisTime,now);
        XxlJobHelper.log("执行定时任务taskExecutor:: 需要同步的文章size={}, from={},to={}", apArticles==null?0:apArticles.size(),redisTime,now);
        if (apArticles != null && !apArticles.isEmpty()) {
            articleDocumentRepository.saveAll(apArticles);
        }
        log.info("EsArticleSync 执行完成....");
        XxlJobHelper.log("EsArticleSync 执行完成....");
    }
}
