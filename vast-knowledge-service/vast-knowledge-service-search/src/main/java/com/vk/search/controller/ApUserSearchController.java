package com.vk.search.controller;

import com.vk.article.domain.HomeArticleListVo;
import com.vk.common.core.web.domain.AjaxResult;
import com.vk.search.service.ApUserSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * APP用户搜索信息 控制层。
 *
 * @author 张三
 * @since 2024-05-13
 */
@RestController
@RequestMapping("/search")
public class ApUserSearchController {

    @Autowired
    private ApUserSearchService apUserSearchService;

    /**
     * @param page 页数
     * @param size 长度
     * @param query  搜索内容
     * @param type   头部标题 0 综合     1 文章  3 标签 4 用户
     * @param sort   排序    0 综合排序  1 最新  2 最热
     * @param period 时间    1 不限      2 最新一天  3 最近一周  4最近一月
     * @return
     */
    @GetMapping("/searchInfo")
    public AjaxResult searchInfo(
            @RequestParam(name = "page",defaultValue = "1",required = false) Long page ,
            @RequestParam(name = "size" ,defaultValue = "10",required = false) Long size ,
            @RequestParam(name = "query") String query,
            @RequestParam(name = "type", defaultValue = "0", required = false) Integer type,
            @RequestParam(name = "sort", defaultValue = "0", required = false) Integer sort,
            @RequestParam(name = "period", defaultValue = "1", required = false) Integer period
    ) {

        List<HomeArticleListVo> result=apUserSearchService.searchInfo(query,type,sort,period,page,size);
        return AjaxResult.success(result);
    }


}
