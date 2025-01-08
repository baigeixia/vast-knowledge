package com.vk.analyze.domain.vo;

import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class VisitorListVo {
    /**
     * 时间
     */
    private List<LocalDate> dailyDataTime=new ArrayList<>();
    /**
     * 日活
     */
    private List<Long> dailyRowData=new ArrayList<>();
    /**
     * 访问量
     */
    private List<ChartDateTime> visitsData=new ArrayList<>();
    /**
     * 注册量
     */
    private List<ChartDateTime> registrationsData=new ArrayList<>();
}
