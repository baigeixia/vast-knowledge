package com.vk.wemedia.controller;

import com.mybatisflex.core.paginate.Page;
import com.vk.wemedia.domain.WmNewsStatistics;
import com.vk.wemedia.service.WmNewsStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * 自媒体图文数据统计 控制层。
 *
 * @author 张三
 * @since 2024-05-13
 */
@RestController
@RequestMapping("/wmNewsStatistics")
public class WmNewsStatisticsController {

    @Autowired
    private WmNewsStatisticsService wmNewsStatisticsService;

    /**
     * 添加自媒体图文数据统计。
     *
     * @param wmNewsStatistics 自媒体图文数据统计
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody WmNewsStatistics wmNewsStatistics) {
        return wmNewsStatisticsService.save(wmNewsStatistics);
    }

    /**
     * 根据主键删除自媒体图文数据统计。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Serializable id) {
        return wmNewsStatisticsService.removeById(id);
    }

    /**
     * 根据主键更新自媒体图文数据统计。
     *
     * @param wmNewsStatistics 自媒体图文数据统计
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody WmNewsStatistics wmNewsStatistics) {
        return wmNewsStatisticsService.updateById(wmNewsStatistics);
    }

    /**
     * 查询所有自媒体图文数据统计。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public List<WmNewsStatistics> list() {
        return wmNewsStatisticsService.list();
    }

    /**
     * 根据自媒体图文数据统计主键获取详细信息。
     *
     * @param id 自媒体图文数据统计主键
     * @return 自媒体图文数据统计详情
     */
    @GetMapping("getInfo/{id}")
    public WmNewsStatistics getInfo(@PathVariable Serializable id) {
        return wmNewsStatisticsService.getById(id);
    }

    /**
     * 分页查询自媒体图文数据统计。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<WmNewsStatistics> page(Page<WmNewsStatistics> page) {
        return wmNewsStatisticsService.page(page);
    }

}
