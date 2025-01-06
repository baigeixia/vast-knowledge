package com.vk.article.jobhandler;

import com.mybatisflex.core.row.Db;
import com.vk.analyze.feign.RemoteChannelService;
import com.vk.article.domain.ApRejection;
import com.vk.article.mapper.ApArticleMapper;
import com.vk.article.mapper.ApRejectionMapper;
import com.vk.common.core.domain.R;
import com.vk.common.core.domain.ValidationUtils;
import com.vk.common.redis.utlis.SensitiveWord;
import com.vk.common.core.utils.threads.TaskVirtualExecutorUtil;
import com.vk.common.core.utils.uuid.UUID;
import com.vk.db.domain.article.ArticleMg;
import com.vk.db.repository.article.ArticleMgRepository;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@Component
@Slf4j(topic = "ArticleReviewJob")
public class ArticleReviewJob {

    @Autowired
    private ApArticleMapper apArticleMapper;

    @Autowired
    private ApRejectionMapper apRejectionMapper;

    @Autowired
    private ArticleMgRepository articleMgRepository;

    @Autowired
    private RemoteChannelService remoteChannelService;

    private final Lock lock = new ReentrantLock();
    @XxlJob("articleReview")
    public void articleReview() {
        UUID uuid = UUID.fastUUID();
        log.info("开始文章审核 uuid:{}", uuid);
        Long count = apArticleMapper.getAuditCount();
        log.info(" 文章审核 uuid:{} -- 审核数量：{}", uuid, count);

        if (count != null && count > 0) {
            // 确定虚拟线程数量
            int numThreads = Math.min(10, count.intValue());
            List<Callable<Void>> tasks = getCallables(count, numThreads);

            ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

            try {
                // 执行所有虚拟线程任务
                executor.invokeAll(tasks);
            } catch ( InterruptedException   e) {
                Thread.currentThread().interrupt();
                log.error("任务被中断", e);
            } finally {
                executor.shutdown();
                try {
                    if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                        log.warn("Executor did not terminate in the allotted time.");
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    log.error("Executor termination interrupted: {}", e.getMessage(), e);
                }
            }
        }

        log.info("文章审核结束 uuid:{}", uuid);
    }

    private List<Callable<Void>> getCallables(Long count, int numThreads) {
        // 如果任务总数少于线程数，动态调整线程数
        long tasksPerThread = count / numThreads;

        // 启动虚拟线程进行审核
        List<Callable<Void>> tasks = new ArrayList<>();
        for (int i = 0; i < numThreads; i++) {
            long startIndex = i * tasksPerThread;
            long endIndex = (i == numThreads - 1) ? count : (i + 1) * tasksPerThread;
            tasks.add(() -> {
                // 每个线程处理一部分任务
                processAuditTasks(startIndex, endIndex);
                return null;
            });
        }
        return tasks;
    }

    private void processAuditTasks(long startIndex, long endIndex) {
        log.info("开始处理第 {} 到第 {} 个审核任务", startIndex, endIndex);
        // 这里是审核任务的处理逻辑
        List<Long> articleIds = apArticleMapper.getAuditArticleId(startIndex, endIndex);

        List<ApRejection> apRejections = new ArrayList<>();
        Set<Long> approvedIds = new HashSet<>();


        for (Long articleId : articleIds) {

            TaskVirtualExecutorUtil.executeWith(() -> {
                try {
                    processArticle(articleId, apRejections,approvedIds);
                } catch (Exception e) {
                    log.error("处理文章 {} 时出现错误: {}", articleId, e.getMessage());
                }
            });
        }

        lock.lock();
        try {

            log.info("--->>> 过滤数量：{}",apRejections.size());
            if (!apRejections.isEmpty()) {
                Set<Long> idS = apRejections.stream().map(ApRejection::getArticleId).collect(Collectors.toSet());
                Db.tx(()->{
                    apArticleMapper.upStatus(idS,3);
                    apRejectionMapper.insertBatch(apRejections);
                    return true;
                });
            }

            log.info("--->>>  通过数量：{}",approvedIds.size());
            if (!approvedIds.isEmpty()) {
                apArticleMapper.pushArticle(approvedIds,9, LocalDateTime.now());
            }
        } finally {
            lock.unlock();
        }
    }




    private void processArticle(Long articleId, List<ApRejection> apRejections,Set<Long> approvedIds) {
        log.info("敏感词检测 文章id=： {}",articleId);
        ArticleMg repository = articleMgRepository.findByArticleId(articleId);
        String content = repository.getContent();
        R<Map<String, AtomicInteger>> sensitiveShort = remoteChannelService.getSensitiveShort(content);
        if (ValidationUtils.validateRSuccess(sensitiveShort)) {
            Map<String, AtomicInteger> shortData = sensitiveShort.getData();
            log.info("敏感词检测结束 文章id=： {}",articleId);
            if (!ObjectUtils.isEmpty(shortData)) {
                ApRejection rejection = new ApRejection();
                rejection.setArticleId(articleId);
                rejection.setRejection("- 出现违禁词 -");
                StringBuffer buffer = new StringBuffer();
                shortData.forEach((word, count) -> buffer.append(word).append("出现：").append(count).append("次").append(","));
                log.info("敏感词检测结束 敏感词 : {}", buffer);
                rejection.setProhibited(buffer.toString());
                apRejections.add(rejection);
            }else {
                approvedIds.add(articleId);
            }
        }
    }


}
