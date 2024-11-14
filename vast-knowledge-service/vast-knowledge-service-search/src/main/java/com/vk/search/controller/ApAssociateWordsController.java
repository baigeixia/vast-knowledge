package com.vk.search.controller;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.vk.common.core.web.domain.AjaxResult;
import com.vk.common.redis.service.RedisService;
import com.vk.search.domain.ApAssociateWords;
import com.vk.search.domain.table.ApAssociateWordsTableDef;
import com.vk.search.domain.vo.AssociateListVo;
import com.vk.search.service.ApAssociateWordsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static com.vk.search.domain.table.ApAssociateWordsTableDef.AP_ASSOCIATE_WORDS;

/**
 * 联想词 控制层。
 *
 * @author 张三
 * @since 2024-05-13
 */
@RestController
@RequestMapping("/associate")
@Slf4j
public class ApAssociateWordsController {

    @Autowired
    private ApAssociateWordsService apAssociateWordsService;
    @Autowired
    private RedisService redisService;
    private String endTimeStr = null;
    private static final Long size = 5000L;
    public static final DateTimeFormatter FORMATTER_YMDHMS = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static volatile boolean sync_status = false;

    /**
     * 同步联想词。
     *
     * @return 所有数据
     */
    @GetMapping("syncAll")
    public AjaxResult sync() {
        Long startTime = System.currentTimeMillis();
        LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault());
        String startTimeStr = now.format(FORMATTER_YMDHMS);
        log.info("全量同步start：{}",startTimeStr);
        Boolean flag = redisService.cacheObjectIfAbsent("associateTime", startTimeStr);
        log.info("全量同步start：{}, redisFlag={}",startTimeStr, flag);
        if (Boolean.TRUE.equals(flag)) {
            try {
                sync_status = true;
                //1.查询统计所有数量
                // Long total = apAssociateWordsService.selectCount(now);
                Long total = apAssociateWordsService.getMapper().selectCountByQuery(
                        QueryWrapper.create().where(AP_ASSOCIATE_WORDS.CREATED_TIME.le(now))
                );
                long totalPages = total % size > 0 ? (total / size) + 1 : total / size;
                final CountDownLatch countDownLatch = new CountDownLatch((int) totalPages);
                //2.执行多线程方法
                for (long page = 1L; page <= totalPages; page++) {
                    apAssociateWordsService.importAll(page, size, countDownLatch,now);
                }
                //3.设置等待 等到 CountDownLatch中的数量减为0之后执行
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    log.error("全量同步error countDownLatch await ：{}",e.getMessage());
                }
               sync_status = false;
            }catch (Exception e){
               sync_status = false;
                log.error("全量同步error：{}, redisFlag={}",startTimeStr, flag, e);
                return AjaxResult.error("发生异常了: " + e.getMessage());
            }
            Long endTime = System.currentTimeMillis();
            endTimeStr = now.format(FORMATTER_YMDHMS);
            log.info("全量同步end：{}, redisFlag={},cost={}",startTimeStr, flag,(endTime - startTime));
        }else {
            return AjaxResult.success("正在执行中....");
        }
        return AjaxResult.success("全量同步已完成了,start=" + startTimeStr + ",end=" + endTimeStr);
    }

    /**
     * 同步联想词。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public AjaxResult list(
            @RequestParam(name = "text")String text
    ) {
       List<AssociateListVo> result= apAssociateWordsService.getESList(text);
        return AjaxResult.success(result);
    }

}
