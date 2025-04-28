package com.vk.search.jobhandler;


import com.vk.common.core.utils.StringUtils;
import com.vk.common.es.domain.AssociateWordsDocument;
import com.vk.common.es.repository.AssociateWordsDocumentRepository;
import com.vk.common.redis.service.RedisService;
import com.vk.search.mapper.ApAssociateWordsMapper;
import com.vk.search.util.SyncUtil;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j(topic = "EsAssociateSearchXxlJob")
public class EsAssociateSearchXxlJob {

    @Autowired
    private RedisService redisService;

    @Autowired
    private ApAssociateWordsMapper apAssociateWordsMapper;

    @Autowired
    private AssociateWordsDocumentRepository associateWordsDocumentRepository;

    /**
     * 1、简单任务示例（Bean模式）
     */
    @XxlJob("esAssociateSearchSync")
    public void esAssociateSearchSync() {
        String recordTime = redisService.getCacheObject("associateTime");
        if (StringUtils.isEmpty(recordTime)) {
            log.info("执行定时任务esAssociateSearchSync: 全量同步未执行,此次不执行");
            XxlJobHelper.log("执行定时任务esAssociateSearchSync: 全量同步未执行,此次不执行");
            return;
        }
        if (SyncUtil.sync_status) {
            log.info("执行定时任务esAssociateSearchSync: 全量同步已执行,但未完成，此次不执行");
            XxlJobHelper.log("执行定时任务esAssociateSearchSync: 全量同步已执行,但未完成，此次不执行");
            return;
        }
        log.info("执行定时任务esAssociateSearchSync:: 执行时间" + LocalDateTime.now());
        XxlJobHelper.log("执行定时任务esAssociateSearchSync:: 执行时间" + LocalDateTime.now());
        // 格式化数据
        // LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault());
        LocalDateTime now = LocalDateTime.now();
        redisService.setCacheObject("associateTime", now.format(SyncUtil.FORMATTER_YMDHMS));
        LocalDateTime redisTime = LocalDateTime.parse(recordTime, SyncUtil.FORMATTER_YMDHMS);
        List<AssociateWordsDocument> apSearch = apAssociateWordsMapper.selectForCondition(redisTime,now);
        log.info("执行定时任务esAssociateSearchSync:: 需要同步的联想词size={}, from={},to={}", apSearch == null ? 0 : apSearch.size(), redisTime, now);
        XxlJobHelper.log("执行定时任务esAssociateSearchSync:: 需要同步的联想词size={}, from={},to={}", apSearch == null ? 0 : apSearch.size(), redisTime, now);
        if (apSearch != null && !apSearch.isEmpty()) {
            associateWordsDocumentRepository.saveAll(apSearch);
        }
        log.info("esAssociateSearchSync 执行完成....");
        XxlJobHelper.log("esAssociateSearchSync 执行完成....");
    }
}
