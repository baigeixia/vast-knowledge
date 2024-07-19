package com.vk.user.controller;

import com.mybatisflex.core.paginate.Page;
import com.vk.user.domain.ApUserArticleList;
import com.vk.user.service.ApUserArticleListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * APP用户文章列 控制层。
 *
 * @author 张三
 * @since 2024-05-13
 */
@RestController
@RequestMapping("/UserArticleList")
public class ApUserArticleListController {

    @Autowired
    private ApUserArticleListService apUserArticleListService;


    /**
     * 添加APP用户文章列。
     *
     * @param apUserArticleList APP用户文章列
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody ApUserArticleList apUserArticleList) {
        return apUserArticleListService.save(apUserArticleList);
    }

    /**
     * 根据主键删除APP用户文章列。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Serializable id) {
        return apUserArticleListService.removeById(id);
    }

    /**
     * 根据主键更新APP用户文章列。
     *
     * @param apUserArticleList APP用户文章列
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody ApUserArticleList apUserArticleList) {
        return apUserArticleListService.updateById(apUserArticleList);
    }

    /**
     * 查询所有APP用户文章列。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public List<ApUserArticleList> list() {
        return apUserArticleListService.list();
    }

    /**
     * 根据APP用户文章列主键获取详细信息。
     *
     * @param id APP用户文章列主键
     * @return APP用户文章列详情
     */
    @GetMapping("getInfo/{id}")
    public ApUserArticleList getInfo(@PathVariable Serializable id) {
        return apUserArticleListService.getById(id);
    }

    /**
     * 分页查询APP用户文章列。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<ApUserArticleList> page(Page<ApUserArticleList> page) {
        return apUserArticleListService.page(page);
    }

}
