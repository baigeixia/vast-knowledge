package com.vk.behaviour.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.vk.behaviour.domain.ApReadBehavior;
import com.vk.behaviour.domain.vo.LocalReadSearchVo;
import com.vk.behaviour.domain.vo.ReadDataAnalysisVo;
import com.vk.behaviour.domain.vo.UserFootMarkListVo;
import com.vk.behaviour.service.ApReadBehaviorService;
import com.vk.common.core.utils.RequestContextUtil;
import com.vk.common.core.web.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

/**
 * APP阅读行为 控制层。
 *
 * @author 张三
 * @since 2024-05-13
 */
@RestController
@RequestMapping("/read")
public class ApReadBehaviorController {

    @Autowired
    private ApReadBehaviorService apReadBehaviorService;


    @GetMapping("/userFootMark")
    public AjaxResult getUserFootMark(
            @RequestParam(name = "page", defaultValue = "1", required = false) Long page,
            @RequestParam(name = "size", defaultValue = "5", required = false) Long size
    ) {
        List<UserFootMarkListVo> result = apReadBehaviorService.getUserFootMark(page, size);
        return AjaxResult.success(result);
    }


    @GetMapping("/article")
    public AjaxResult getArticleInfo(
            @RequestParam(name = "id") Long id
    ) {
        ApReadBehavior result = apReadBehaviorService.getArticleInfo(id);
        return AjaxResult.success(result);
    }

    @GetMapping("/search")
    public AjaxResult searchRead(
            @RequestParam(name = "query") String query,
            @RequestParam(name = "page", defaultValue = "1", required = false) Long page,
            @RequestParam(name = "size", defaultValue = "10", required = false) Long size
    ) {
        LocalReadSearchVo result = apReadBehaviorService.searchRead(query, page, size);
        return AjaxResult.success(result);
    }

    @GetMapping("/clearAll")
    public AjaxResult clearAll(
    ) {
        Long localUserId = RequestContextUtil.getUserId();
        apReadBehaviorService.clearAll(localUserId);
        return AjaxResult.success();
    }

    /**
     *
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param cycle 查询天数
     * @return 数据详情
     * 0 今天
     * 1 本周
     * 2 近7天
     * 3 近30天
     */
    @GetMapping("/readDataAnalysis")
    public AjaxResult readDataAnalysis(
            @RequestParam(name = "articleId") Long articleId,
            @RequestParam(name = "startTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @RequestParam(name = "endTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime,
            @RequestParam(name = "cycle", required = false, defaultValue = "0") Integer cycle
    ) {
        ReadDataAnalysisVo result=apReadBehaviorService.readDataAnalysis(articleId,startTime,endTime,cycle);
        return AjaxResult.success(result);
    }


}
