package com.vk.article.controller.sync;

import com.vk.article.service.ApArticleService;
import com.vk.article.util.SyncUtil;
import com.vk.common.core.web.domain.AjaxResult;
import com.vk.common.redis.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.concurrent.CountDownLatch;

@RestController
@RequestMapping("/sync")
@Slf4j
public class SyncController {
    @Autowired
    private ApArticleService apArticleService;
    @Autowired
    private RedisService redisService;
    private String endTimeStr = null;
    private static final Long size = 5000L;

//    @RequiresPermissions("system:user:edit")
    @GetMapping("/importAll")
    public AjaxResult importAll() {

        Long startTime = System.currentTimeMillis();
        LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault());
        String startTimeStr = now.format(SyncUtil.FORMATTER_YMDHMS);
        log.info("全量同步start：{}",startTimeStr);
        Boolean flag = redisService.cacheObjectIfAbsent("recordTime", startTimeStr);
        log.info("全量同步start：{}, redisFlag={}",startTimeStr, flag);
        if (Boolean.TRUE.equals(flag)) {
            try {
                SyncUtil.sync_status = true;
                //1.查询统计所有数量
                Long total = apArticleService.selectCount(now);
                long totalPages = total % size > 0 ? (total / size) + 1 : total / size;
                final CountDownLatch countDownLatch = new CountDownLatch((int) totalPages);
                //2.执行多线程方法
                for (long page = 1L; page <= totalPages; page++) {
                    apArticleService.importAll(page, size, countDownLatch,now);
                }
                //3.设置等待 等到 CountDownLatch中的数量减为0之后执行
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    log.error("全量同步error countDownLatch await ：{}",e.getMessage());
                }
                SyncUtil.sync_status = false;
            }catch (Exception e){
                SyncUtil.sync_status = false;
                log.error("全量同步error：{}, redisFlag={}",startTimeStr, flag, e);
                return AjaxResult.error("发生异常了: " + e.getMessage());
            }
            Long endTime = System.currentTimeMillis();
            endTimeStr = now.format(SyncUtil.FORMATTER_YMDHMS);
            log.info("全量同步end：{}, redisFlag={},cost={}",startTimeStr, flag,(endTime - startTime));
        }else {
            return AjaxResult.success("正在执行中....");
        }
        return AjaxResult.success("全量同步已完成了,start=" + startTimeStr + ",end=" + endTimeStr);
    }
}
