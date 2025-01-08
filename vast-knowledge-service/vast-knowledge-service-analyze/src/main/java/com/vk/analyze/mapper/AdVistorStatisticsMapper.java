package com.vk.analyze.mapper;

import com.mybatisflex.core.BaseMapper;
import com.vk.analyze.domain.AdVistorStatistics;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 访问数据统计 映射层。
 *
 * @author 张三
 * @since 2024-05-13
 */
public interface AdVistorStatisticsMapper extends BaseMapper<AdVistorStatistics> {

    @Select(
            "select activity,vistor,ip,register,created_time as createdTime from  ad_vistor_statistics where  created_time between #{start} and #{end} order by created_time ASC"
    )
    @MapKey("createdTime")
    Map<LocalDate, AdVistorStatistics> getTimeDataMap(@Param("start") LocalDate startTime, @Param("end")LocalDate endTime);
}
