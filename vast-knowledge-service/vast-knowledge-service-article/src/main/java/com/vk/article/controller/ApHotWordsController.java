package com.vk.article.controller;

import com.mybatisflex.core.paginate.Page;
import com.vk.article.domain.ApHotWords;
import com.vk.article.service.ApHotWordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * 搜索热词 控制层。
 *
 * @author 张三
 * @since 2024-07-11
 */
@RestController
@RequestMapping("/HotWords")
public class ApHotWordsController {

    @Autowired
    private ApHotWordsService apHotWordsService;

    /**
     * 添加搜索热词。
     *
     * @param apHotWords 搜索热词
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody ApHotWords apHotWords) {
        return apHotWordsService.save(apHotWords);
    }

    /**
     * 根据主键删除搜索热词。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Serializable id) {
        return apHotWordsService.removeById(id);
    }

    /**
     * 根据主键更新搜索热词。
     *
     * @param apHotWords 搜索热词
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody ApHotWords apHotWords) {
        return apHotWordsService.updateById(apHotWords);
    }

    /**
     * 查询所有搜索热词。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public List<ApHotWords> list() {
        return apHotWordsService.list();
    }

    /**
     * 根据搜索热词主键获取详细信息。
     *
     * @param id 搜索热词主键
     * @return 搜索热词详情
     */
    @GetMapping("getInfo/{id}")
    public ApHotWords getInfo(@PathVariable Serializable id) {
        return apHotWordsService.getById(id);
    }

    /**
     * 分页查询搜索热词。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<ApHotWords> page(Page<ApHotWords> page) {
        return apHotWordsService.page(page);
    }

}
