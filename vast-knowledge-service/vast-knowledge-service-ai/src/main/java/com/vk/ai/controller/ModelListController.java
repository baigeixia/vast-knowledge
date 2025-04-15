package com.vk.ai.controller;

import com.mybatisflex.core.paginate.Page;
import com.vk.ai.domain.ModelList;
import com.vk.ai.service.ModelListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 *  控制层。
 *
 * @author 张三
 * @since 2025-04-15
 */
@RestController
@RequestMapping("/modelList")
public class ModelListController {

    @Autowired
    private ModelListService modelListService;

    /**
     * 添加。
     *
     * @param modelList 
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody ModelList modelList) {
        LocalDateTime dateTime = LocalDateTime.now();
        modelList.setCreatingTime(dateTime);
        modelList.setUpdateTime(dateTime);
        return modelListService.save(modelList);
    }

    /**
     * 根据主键删除。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Serializable id) {
        return modelListService.removeById(id);
    }

    /**
     * 根据主键更新。
     *
     * @param modelList 
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody ModelList modelList) {
        return modelListService.updateById(modelList);
    }

    /**
     * 查询所有。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public List<ModelList> list() {
        return modelListService.list();
    }

    /**
     * 根据主键获取详细信息。
     *
     * @param id 主键
     * @return 详情
     */
    @GetMapping("getInfo/{id}")
    public ModelList getInfo(@PathVariable Serializable id) {
        return modelListService.getById(id);
    }

    /**
     * 分页查询。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<ModelList> page(Page<ModelList> page) {
        return modelListService.page(page);
    }

}
