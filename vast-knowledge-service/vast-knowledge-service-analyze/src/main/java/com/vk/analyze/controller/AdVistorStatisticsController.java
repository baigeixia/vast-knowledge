package com.vk.analyze.controller;

import com.mybatisflex.core.paginate.Page;
import com.vk.analyze.domain.AdVistorStatistics;
import com.vk.analyze.service.AdVistorStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * 访问数据统计 控制层。
 *
 * @author 张三
 * @since 2024-05-13
 */
@RestController
@RequestMapping("/VistorStatistics")
public class AdVistorStatisticsController {

    @Autowired
    private AdVistorStatisticsService adVistorStatisticsService;

    /**
     * 添加访问数据统计。
     *
     * @param adVistorStatistics 访问数据统计
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody AdVistorStatistics adVistorStatistics) {
        return adVistorStatisticsService.save(adVistorStatistics);
    }

    /**
     * 根据主键删除访问数据统计。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Serializable id) {
        return adVistorStatisticsService.removeById(id);
    }

    /**
     * 根据主键更新访问数据统计。
     *
     * @param adVistorStatistics 访问数据统计
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody AdVistorStatistics adVistorStatistics) {
        return adVistorStatisticsService.updateById(adVistorStatistics);
    }

    /**
     * 查询所有访问数据统计。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public List<AdVistorStatistics> list() {
        return adVistorStatisticsService.list();
    }

    /**
     * 根据访问数据统计主键获取详细信息。
     *
     * @param id 访问数据统计主键
     * @return 访问数据统计详情
     */
    @GetMapping("getInfo/{id}")
    public AdVistorStatistics getInfo(@PathVariable Serializable id) {
        return adVistorStatisticsService.getById(id);
    }

    /**
     * 分页查询访问数据统计。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<AdVistorStatistics> page(Page<AdVistorStatistics> page) {
        return adVistorStatisticsService.page(page);
    }

}
