package com.vk.analyze.service;

import com.mybatisflex.core.service.IService;
import com.vk.analyze.domain.AdVistorStatistics;
import com.vk.analyze.domain.vo.VisitorListVo;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 访问数据统计 服务层。
 *
 * @author 张三
 * @since 2024-05-13
 */
public interface AdVistorStatisticsService extends IService<AdVistorStatistics> {

    VisitorListVo chartLIstDate(LocalDate startTime, LocalDate endTime, Integer cycle);
}
