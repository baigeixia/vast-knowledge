package com.vk.article.controller;

import com.mybatisflex.core.paginate.Page;
import com.vk.article.domain.ApCollection;
import com.vk.article.service.ApCollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * APP收藏信息 控制层。
 *
 * @author 张三
 * @since 2024-07-11
 */
@RestController
@RequestMapping("/apCollection")
public class ApCollectionController {

    @Autowired
    private ApCollectionService apCollectionService;

    /**
     * 添加APP收藏信息。
     *
     * @param apCollection APP收藏信息
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody ApCollection apCollection) {
        return apCollectionService.save(apCollection);
    }

    /**
     * 根据主键删除APP收藏信息。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Serializable id) {
        return apCollectionService.removeById(id);
    }

    /**
     * 根据主键更新APP收藏信息。
     *
     * @param apCollection APP收藏信息
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody ApCollection apCollection) {
        return apCollectionService.updateById(apCollection);
    }

    /**
     * 查询所有APP收藏信息。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public List<ApCollection> list() {
        return apCollectionService.list();
    }

    /**
     * 根据APP收藏信息主键获取详细信息。
     *
     * @param id APP收藏信息主键
     * @return APP收藏信息详情
     */
    @GetMapping("getInfo/{id}")
    public ApCollection getInfo(@PathVariable Serializable id) {
        return apCollectionService.getById(id);
    }

    /**
     * 分页查询APP收藏信息。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<ApCollection> page(Page<ApCollection> page) {
        return apCollectionService.page(page);
    }

}
