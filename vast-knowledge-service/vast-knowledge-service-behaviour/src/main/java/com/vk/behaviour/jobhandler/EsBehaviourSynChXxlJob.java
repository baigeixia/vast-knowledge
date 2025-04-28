package com.vk.behaviour.jobhandler;


import com.vk.behaviour.mapper.ApReadBehaviorMapper;
import com.vk.behaviour.service.ApReadBehaviorService;
import com.vk.behaviour.util.SyncUtil;
import com.vk.common.core.utils.StringUtils;
import com.vk.common.es.domain.UserReadDocument;
import com.vk.common.es.repository.UserReadDocumentRepository;
import com.vk.common.redis.service.RedisService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j(topic = "EsBehaviourSynChXxlJob")
public class EsBehaviourSynChXxlJob {

    @Autowired
    private RedisService redisService;
    @Autowired
    private UserReadDocumentRepository userReadDocumentRepository;
    @Autowired
    private ApReadBehaviorService apReadBehaviorService;

    /**
     * 1、简单任务示例（Bean模式）
     */
    @XxlJob("esUserReadSync")
    public void esUserReadSync() {
        String recordTime = redisService.getCacheObject("recordUserReadTime");
        if (StringUtils.isEmpty(recordTime)) {
            log.info("执行定时任务EsUserInfoSync: 全量同步未执行,此次不执行");
            XxlJobHelper.log("执行定时任务EsUserInfoSync: 全量同步未执行,此次不执行");
            return;
        }
        if (SyncUtil.sync_status) {
            log.info("执行定时任务EsUserInfoSync: 全量同步已执行,但未完成，此次不执行");
            XxlJobHelper.log("执行定时任务EsUserInfoSync: 全量同步已执行,但未完成，此次不执行");
            return;
        }
        log.info("执行定时任务EsUserInfoSync:: 执行时间" + LocalDateTime.now());
        XxlJobHelper.log("执行定时任务EsUserInfoSync:: 执行时间" + LocalDateTime.now());
        // 格式化数据
        // LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault());
        LocalDateTime now = LocalDateTime.now();
        redisService.setCacheObject("recordUserReadTime", now.format(SyncUtil.FORMATTER_YMDHMS));
        LocalDateTime redisTime = LocalDateTime.parse(recordTime, SyncUtil.FORMATTER_YMDHMS);
        List<UserReadDocument> userReadDocuments = apReadBehaviorService.selectForCondition(redisTime, now);
        log.info("执行定时任务EsUserInfoSync:: 需要同步的文章size={}, from={},to={}", userReadDocuments == null ? 0 : userReadDocuments.size(), redisTime, now);
        XxlJobHelper.log("执行定时任务EsUserInfoSync:: 需要同步的文章size={}, from={},to={}", userReadDocuments == null ? 0 : userReadDocuments.size(), redisTime, now);
        if (userReadDocuments != null && !userReadDocuments.isEmpty()) {
            userReadDocumentRepository.saveAll(userReadDocuments);
        }
        log.info("EsArticleSync 执行完成....");
        XxlJobHelper.log("EsArticleSync 执行完成....");
    }
}
