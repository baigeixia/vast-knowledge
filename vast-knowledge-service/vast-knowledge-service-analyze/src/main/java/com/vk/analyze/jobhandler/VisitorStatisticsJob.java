package com.vk.analyze.jobhandler;

import com.vk.analyze.domain.AdVistorStatistics;
import com.vk.analyze.mapper.AdVistorStatisticsMapper;
import com.vk.common.core.constant.VisitorStatisticsConstant;
import com.vk.common.core.utils.DateUtils;
import com.vk.common.core.utils.uuid.UUID;
import com.vk.common.redis.service.RedisService;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Component
@Slf4j(topic = "VisitorStatisticsJob")
public class VisitorStatisticsJob {

    @Autowired
    private RedisService redisService;

    @Autowired
    private AdVistorStatisticsMapper adVistorStatisticsMapper;

    private final Lock lock = new ReentrantLock();

    @XxlJob("visitorStatistics")
    public void visitorStatistics() {
        UUID uuid = UUID.fastUUID();
        String date = DateUtils.getDate();
        log.info("统计数据开始 uuid:{} date:{}", uuid, date);

        lock.lock();

        try {
            Long dau = null;
            Long pv = null;
            Long registrations = null;
            try {
                dau = redisService.getBit(VisitorStatisticsConstant.getVisitorDauKey());
                pv = Integer.toUnsignedLong(redisService.getCacheObject(VisitorStatisticsConstant.getVisitorPvKey()));
                Integer register = redisService.getCacheObject(VisitorStatisticsConstant.getVisitorRegistrationsKey());
                registrations = register != null ? Integer.toUnsignedLong(register) : 0L;
            } catch (Exception e) {
                log.error("统计数据--> 获取redis数据错误 error：{}", e.getMessage());
            }

            AdVistorStatistics adVistorStatistics = new AdVistorStatistics();
            adVistorStatistics.setActivity(dau);
            adVistorStatistics.setVistor(pv);
            adVistorStatistics.setRegister(registrations);
            adVistorStatistics.setCreatedTime(LocalDate.now());

            try {
                adVistorStatisticsMapper.insertSelective(adVistorStatistics);
            } catch (Exception e) {
                log.error("统计数据--> 数据库储存错误 error：{}", e.getMessage());
            }
        } finally {
            lock.unlock();
        }


        log.info("统计数据结束 uuid:{} date:{}", uuid, date);
    }

}
