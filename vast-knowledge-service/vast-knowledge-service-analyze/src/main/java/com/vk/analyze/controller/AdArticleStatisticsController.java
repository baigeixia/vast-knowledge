package com.vk.analyze.controller;

import com.mybatisflex.core.paginate.Page;
import com.vk.analyze.domain.AdArticleStatistics;
import com.vk.analyze.service.AdArticleStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * 文章数据统计 控制层。
 *
 * @author 张三
 * @since 2024-05-13
 */
@RestController
@RequestMapping("/ArticleStatistics")
public class AdArticleStatisticsController {

    @Autowired
    private AdArticleStatisticsService adArticleStatisticsService;

    /**
     * 添加文章数据统计。
     *
     * @param adArticleStatistics 文章数据统计
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody AdArticleStatistics adArticleStatistics) {
        return adArticleStatisticsService.save(adArticleStatistics);
    }

    /**
     * 根据主键删除文章数据统计。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Serializable id) {
        return adArticleStatisticsService.removeById(id);
    }

    /**
     * 根据主键更新文章数据统计。
     *
     * @param adArticleStatistics 文章数据统计
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody AdArticleStatistics adArticleStatistics) {
        return adArticleStatisticsService.updateById(adArticleStatistics);
    }

    /**
     * 查询所有文章数据统计。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public List<AdArticleStatistics> list() {
        return adArticleStatisticsService.list();
    }

    /**
     * 根据文章数据统计主键获取详细信息。
     *
     * @param id 文章数据统计主键
     * @return 文章数据统计详情
     */
    @GetMapping("getInfo/{id}")
    public AdArticleStatistics getInfo(@PathVariable Serializable id) {
        return adArticleStatisticsService.getById(id);
    }

    /**
     * 分页查询文章数据统计。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<AdArticleStatistics> page(Page<AdArticleStatistics> page) {
        return adArticleStatisticsService.page(page);
    }

}
