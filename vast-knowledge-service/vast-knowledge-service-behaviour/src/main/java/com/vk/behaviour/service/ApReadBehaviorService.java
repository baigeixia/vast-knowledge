package com.vk.behaviour.service;

import com.mybatisflex.core.service.IService;
import com.vk.behaviour.domain.ApReadBehavior;
import com.vk.behaviour.domain.vo.LocalReadSearchVo;
import com.vk.behaviour.domain.vo.ReadDataAnalysisVo;
import com.vk.behaviour.domain.vo.UserFootMarkListVo;
import com.vk.common.es.domain.UserReadDocument;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * APP阅读行为 服务层。
 *
 * @author 张三
 * @since 2024-05-13
 */
public interface ApReadBehaviorService extends IService<ApReadBehavior> {

    List<UserFootMarkListVo> getUserFootMark(Long page, Long size);

    ApReadBehavior getArticleInfo(Long id);

    Long selectCount(LocalDateTime now);

    void importAll(long page, Long size, CountDownLatch countDownLatch, LocalDateTime now);

    List<UserReadDocument> selectForCondition(LocalDateTime redisTime, LocalDateTime now);

    LocalReadSearchVo searchRead(String query, Long page, Long size);

    void clearAll(Long userid);

    ReadDataAnalysisVo readDataAnalysis(Long articleId,LocalDateTime startTime, LocalDateTime endTime, Integer cycle);
}
