package com.vk.search.controller;

import com.vk.article.domain.HomeArticleListVo;
import com.vk.common.core.web.domain.AjaxResult;
import com.vk.common.redis.service.RedisService;
import com.vk.search.domain.ApHotWords;
import com.vk.search.domain.ApUserSearch;
import com.vk.search.service.ApUserSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    private RedisService redisService;

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
            @RequestParam(name = "query") String query,
            @RequestParam(name = "type", defaultValue = "0", required = false) Integer type,
            @RequestParam(name = "sort", defaultValue = "0", required = false) Integer sort,
            @RequestParam(name = "period", defaultValue = "1", required = false) Integer period,
            @RequestParam(name = "page",defaultValue = "1",required = false) Long page ,
            @RequestParam(name = "size" ,defaultValue = "10",required = false) Long size
    ) {

        List<HomeArticleListVo> result=apUserSearchService.searchInfo(query,type,sort,period,page,size);
        return AjaxResult.success(result);
    }


    @GetMapping("/userSearch")
    public AjaxResult userSearch() {
        List<ApUserSearch> result=apUserSearchService.userSearch();
        return AjaxResult.success(result);
    }

    @GetMapping("/addUserSearch")
    public AjaxResult addUserSearch(
            @RequestParam(name = "query") String query
    ) {
        ApUserSearch result=apUserSearchService.addUserSearch(query);
        return AjaxResult.success(result);
    }



    @DeleteMapping("/rmHistory")
    public AjaxResult rmHistory(
            @RequestParam(name = "id") Long id
    ) {
        apUserSearchService.rmHistory(id);
        return AjaxResult.success();
    }

    @DeleteMapping("/rmHistoryAll")
    public AjaxResult rmHistory(
    ) {
        apUserSearchService.rmHistoryAll();
        return AjaxResult.success();
    }


}

