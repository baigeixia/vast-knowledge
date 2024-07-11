package com.vk.article.controller;

import com.mybatisflex.core.paginate.Page;
import com.vk.article.domain.ApHotArticles;
import com.vk.article.service.ApHotArticlesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * 热点文章 控制层。
 *
 * @author 张三
 * @since 2024-07-11
 */
@RestController
@RequestMapping("/apHotArticles")
public class ApHotArticlesController {

    @Autowired
    private ApHotArticlesService apHotArticlesService;

    /**
     * 添加热点文章。
     *
     * @param apHotArticles 热点文章
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody ApHotArticles apHotArticles) {
        return apHotArticlesService.save(apHotArticles);
    }

    /**
     * 根据主键删除热点文章。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Serializable id) {
        return apHotArticlesService.removeById(id);
    }

    /**
     * 根据主键更新热点文章。
     *
     * @param apHotArticles 热点文章
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody ApHotArticles apHotArticles) {
        return apHotArticlesService.updateById(apHotArticles);
    }

    /**
     * 查询所有热点文章。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public List<ApHotArticles> list() {
        return apHotArticlesService.list();
    }

    /**
     * 根据热点文章主键获取详细信息。
     *
     * @param id 热点文章主键
     * @return 热点文章详情
     */
    @GetMapping("getInfo/{id}")
    public ApHotArticles getInfo(@PathVariable Serializable id) {
        return apHotArticlesService.getById(id);
    }

    /**
     * 分页查询热点文章。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<ApHotArticles> page(Page<ApHotArticles> page) {
        return apHotArticlesService.page(page);
    }

}
