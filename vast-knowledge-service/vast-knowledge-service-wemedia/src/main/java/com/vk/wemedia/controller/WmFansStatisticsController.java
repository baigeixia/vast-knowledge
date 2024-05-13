package com.vk.wemedia.controller;

import com.mybatisflex.core.paginate.Page;
import com.vk.wemedia.domain.WmFansStatistics;
import com.vk.wemedia.service.WmFansStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * 自媒体粉丝数据统计 控制层。
 *
 * @author 张三
 * @since 2024-05-13
 */
@RestController
@RequestMapping("/wmFansStatistics")
public class WmFansStatisticsController {

    @Autowired
    private WmFansStatisticsService wmFansStatisticsService;

    /**
     * 添加自媒体粉丝数据统计。
     *
     * @param wmFansStatistics 自媒体粉丝数据统计
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody WmFansStatistics wmFansStatistics) {
        return wmFansStatisticsService.save(wmFansStatistics);
    }

    /**
     * 根据主键删除自媒体粉丝数据统计。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Serializable id) {
        return wmFansStatisticsService.removeById(id);
    }

    /**
     * 根据主键更新自媒体粉丝数据统计。
     *
     * @param wmFansStatistics 自媒体粉丝数据统计
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody WmFansStatistics wmFansStatistics) {
        return wmFansStatisticsService.updateById(wmFansStatistics);
    }

    /**
     * 查询所有自媒体粉丝数据统计。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public List<WmFansStatistics> list() {
        return wmFansStatisticsService.list();
    }

    /**
     * 根据自媒体粉丝数据统计主键获取详细信息。
     *
     * @param id 自媒体粉丝数据统计主键
     * @return 自媒体粉丝数据统计详情
     */
    @GetMapping("getInfo/{id}")
    public WmFansStatistics getInfo(@PathVariable Serializable id) {
        return wmFansStatisticsService.getById(id);
    }

    /**
     * 分页查询自媒体粉丝数据统计。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<WmFansStatistics> page(Page<WmFansStatistics> page) {
        return wmFansStatisticsService.page(page);
    }

}
