package com.vk.search.domain.dto;

import lombok.Data;

@Data
public class HomeSearchDto {
    /**
     * 搜索内容
     */
    private String query;
    /**
     * 头部标题 0综合  1 文章  3 标签 4 用户
     */
    private String type;
    /**
     *  排序 0 综合排序  1 最新  2 最热
     */
    private Integer sort;
    /**
     *  时间 1 不限  2 最新一天  3 最近一周  4最近一月
     */
    private Integer period;
}
