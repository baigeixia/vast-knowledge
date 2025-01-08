package com.vk.common.core.constant;


import com.vk.common.core.utils.DateUtils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 统计数据使用的常量
 */
public class VisitorStatisticsConstant {
    public final static  String VISITOR_KEY = "visitor_statistics:";
    /**
     * 日活
     */
    public final static  String DAU = "dau:";
    /**
     * 访问
     */
    public final static  String PV = "pv:";
    /**
     * 注册
     */
    public final static  String REGISTRATIONS = "registrations:";

    public  static String getVisitorDauKey(){
        return VISITOR_KEY+DAU+ DateUtils.getDate();
    }

    public  static String getVisitorPvKey(){
        return VISITOR_KEY+PV+DateUtils.getDate();
    }

    public  static String getVisitorRegistrationsKey(){
        return VISITOR_KEY+REGISTRATIONS+DateUtils.getDate();
    }



}
