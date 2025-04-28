package com.vk.user.jobhandler;


import com.vk.common.core.utils.StringUtils;
import com.vk.common.es.domain.UserInfoDocument;
import com.vk.common.es.repository.UserInfoDocumentRepository;
import com.vk.common.redis.service.RedisService;
import com.vk.user.mapper.ApUserInfoMapper;
import com.vk.user.util.SyncUtil;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j(topic="EsUserInfoSynChXxlJob")
public class EsArticleSynChXxlJob {

    @Autowired
    private RedisService redisService;

    @Autowired
    private ApUserInfoMapper apUserInfoMapper;

    @Autowired
    private UserInfoDocumentRepository userInfoDocumentRepository;
    /**
     * 1、简单任务示例（Bean模式）
     */
    @XxlJob("esUserInfoSync")
    public void esUserInfoSync() {
        String recordTime = redisService.getCacheObject("recordTime");
        if (StringUtils.isEmpty(recordTime)) {
            log.info("执行定时任务EsUserInfoSync: 全量同步未执行,此次不执行");
            XxlJobHelper.log("执行定时任务EsUserInfoSync: 全量同步未执行,此次不执行");
            return;
        }
        if(SyncUtil.sync_status){
            log.info("执行定时任务EsUserInfoSync: 全量同步已执行,但未完成，此次不执行");
            XxlJobHelper.log("执行定时任务EsUserInfoSync: 全量同步已执行,但未完成，此次不执行");
            return;
        }
        log.info("执行定时任务EsUserInfoSync:: 执行时间" + LocalDateTime.now());
        XxlJobHelper.log("执行定时任务EsUserInfoSync:: 执行时间" + LocalDateTime.now());
        //格式化数据
        // LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault());
        LocalDateTime now = LocalDateTime.now();
        redisService.setCacheObject("recordUserTime",now.format(SyncUtil.FORMATTER_YMDHMS));
        LocalDateTime redisTime = LocalDateTime.parse(recordTime, SyncUtil.FORMATTER_YMDHMS);
        List<UserInfoDocument> userInfoDocuments = apUserInfoMapper.selectForCondition(redisTime, now);
        log.info("执行定时任务EsUserInfoSync:: 需要同步的文章size={}, from={},to={}", userInfoDocuments ==null?0: userInfoDocuments.size(),redisTime,now);
        XxlJobHelper.log("执行定时任务EsUserInfoSync:: 需要同步的文章size={}, from={},to={}", userInfoDocuments ==null?0: userInfoDocuments.size(),redisTime,now);
        if (userInfoDocuments != null && !userInfoDocuments.isEmpty()) {
            userInfoDocumentRepository.saveAll(userInfoDocuments);
        }
        log.info("EsArticleSync 执行完成....");
        XxlJobHelper.log("EsArticleSync 执行完成....");
    }
}
