package com.vk.search.controller;

import com.mybatisflex.core.paginate.Page;
import com.vk.search.domain.ApUserSearch;
import com.vk.search.service.ApUserSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * APP用户搜索信息 控制层。
 *
 * @author 张三
 * @since 2024-05-13
 */
@RestController
@RequestMapping("/apUserSearch")
public class ApUserSearchController {

    @Autowired
    private ApUserSearchService apUserSearchService;

    /**
     * 添加APP用户搜索信息。
     *
     * @param apUserSearch APP用户搜索信息
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody ApUserSearch apUserSearch) {
        return apUserSearchService.save(apUserSearch);
    }

    /**
     * 根据主键删除APP用户搜索信息。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Serializable id) {
        return apUserSearchService.removeById(id);
    }

    /**
     * 根据主键更新APP用户搜索信息。
     *
     * @param apUserSearch APP用户搜索信息
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody ApUserSearch apUserSearch) {
        return apUserSearchService.updateById(apUserSearch);
    }

    /**
     * 查询所有APP用户搜索信息。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public List<ApUserSearch> list() {
        return apUserSearchService.list();
    }

    /**
     * 根据APP用户搜索信息主键获取详细信息。
     *
     * @param id APP用户搜索信息主键
     * @return APP用户搜索信息详情
     */
    @GetMapping("getInfo/{id}")
    public ApUserSearch getInfo(@PathVariable Serializable id) {
        return apUserSearchService.getById(id);
    }

    /**
     * 分页查询APP用户搜索信息。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<ApUserSearch> page(Page<ApUserSearch> page) {
        return apUserSearchService.page(page);
    }

}
