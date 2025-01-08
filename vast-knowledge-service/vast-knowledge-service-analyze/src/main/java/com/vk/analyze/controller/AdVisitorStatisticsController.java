package com.vk.analyze.controller;

import com.vk.analyze.domain.vo.VisitorListVo;
import com.vk.analyze.service.AdVistorStatisticsService;
import com.vk.common.core.web.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

/**
 * 访问数据统计 控制层。
 *
 * @author 张三
 * @since 2024-05-13
 */
@RestController
@RequestMapping("/visitor")
public class AdVisitorStatisticsController {

    @Autowired
    private AdVistorStatisticsService adVistorStatisticsService;

    /**
     * 查询所有访问数据统计。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public AjaxResult ChartLIstDate(
            // @RequestParam(name = "startTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDateTime startTime,
            // @RequestParam(name = "endTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDateTime endTime,
            @RequestParam(name = "startTime", required = false)  LocalDate startTime,
            @RequestParam(name = "endTime", required = false)  LocalDate endTime,
            @RequestParam(name = "cycle", required = false, defaultValue = "0") Integer cycle
    ) {

        VisitorListVo vo = adVistorStatisticsService.chartLIstDate(startTime,endTime,cycle);

        return AjaxResult.success(vo);
    }


}
