package com.vk.analyze.service.impl;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.vk.analyze.domain.AdVistorStatistics;
import com.vk.analyze.domain.vo.ChartDateTime;
import com.vk.analyze.domain.vo.VisitorListVo;
import com.vk.analyze.mapper.AdVistorStatisticsMapper;
import com.vk.analyze.service.AdVistorStatisticsService;
import com.vk.common.core.exception.LeadNewsException;
import com.vk.common.core.utils.DateUtils;
import com.vk.common.core.utils.threads.TaskVirtualExecutorUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static com.vk.analyze.domain.table.AdVistorStatisticsTableDef.AD_VISTOR_STATISTICS;

/**
 * 访问数据统计 服务层实现。
 *
 * @author 张三
 * @since 2024-05-13
 */
@Service
public class AdVistorStatisticsServiceImpl extends ServiceImpl<AdVistorStatisticsMapper, AdVistorStatistics> implements AdVistorStatisticsService {

    @Override
    public VisitorListVo chartLIstDate(LocalDate startTime, LocalDate endTime, Integer cycle) {
        if (Stream.of(0, 1, 2, 3).noneMatch(Predicate.isEqual(cycle))) {
            throw new LeadNewsException("错误的日期");
        }

        if (!ObjectUtils.isEmpty(startTime) && !ObjectUtils.isEmpty(endTime)) {
            return timeData(startTime, endTime);
        }

        LocalDate cycleStartTime = null;
        LocalDate cycleEndTime = null;

        // 如果 startTime 和 endTime 为空，根据其他时间参数进行处理
        switch (cycle) {
            case 0 -> {
                // 今天
                cycleStartTime = DateUtils.getStartOfDay().toLocalDate();
                cycleEndTime = DateUtils.getEndOfDay().toLocalDate();
            }
            case 1 -> {
                // 本周
                cycleStartTime = DateUtils.getStartOfWeek().toLocalDate();
                cycleEndTime = DateUtils.getEndOfWeek().toLocalDate();
            }
            case 2 -> {
                // 整周
                cycleStartTime = DateUtils.getStartOfWholeWeek().toLocalDate();
                cycleEndTime = DateUtils.getEndOfWholeWeek().toLocalDate();
            }
            case 3 -> {
                // 整月
                cycleStartTime = DateUtils.getStartOfWholeMonth().toLocalDate();
                cycleEndTime = DateUtils.getEndOfWholeMonth().toLocalDate();
            }
        }

        return timeData(cycleStartTime, cycleEndTime);
    }

    private VisitorListVo timeData(LocalDate startTime, LocalDate endTime) {
        return TaskVirtualExecutorUtil.executeWith(() -> {
            VisitorListVo vo = new VisitorListVo();
            Map<LocalDate,AdVistorStatistics> adVisitorStatistics = mapper.getTimeDataMap(startTime,endTime);
            if (null!=adVisitorStatistics){
                Map<LocalDate, AdVistorStatistics> sortedMap = new TreeMap<>(adVisitorStatistics);

                for (LocalDate date  : sortedMap.keySet()) {
                    AdVistorStatistics statistics = adVisitorStatistics.get(date);
                    // 1. 将日期添加到 dailyDataTime 列表
                    vo.getDailyDataTime().add(date );
                    // 2. 将 activity (日活) 添加到 dailyRowData 列表
                    vo.getDailyRowData().add(statistics.getActivity());
                    // 3. 将 vistor (访问量) 添加到 visitsData 列表
                    vo.getVisitsData().add(new ChartDateTime(date, statistics.getVistor()));
                    // 4. 将 register (注册量) 添加到 registrationsData 列表
                    vo.getRegistrationsData().add(new ChartDateTime(date, statistics.getRegister()));
                }
            }
            return vo;
        });
    }
}
