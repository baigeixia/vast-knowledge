package com.vk.analyze.controller;

import com.mybatisflex.core.paginate.Page;
import com.vk.analyze.domain.AdFunction;
import com.vk.analyze.service.AdFunctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * 页面功能信息 控制层。
 *
 * @author 张三
 * @since 2024-05-13
 */
@RestController
@RequestMapping("/adFunction")
public class AdFunctionController {

    @Autowired
    private AdFunctionService adFunctionService;

    /**
     * 添加页面功能信息。
     *
     * @param adFunction 页面功能信息
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody AdFunction adFunction) {
        return adFunctionService.save(adFunction);
    }

    /**
     * 根据主键删除页面功能信息。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Serializable id) {
        return adFunctionService.removeById(id);
    }

    /**
     * 根据主键更新页面功能信息。
     *
     * @param adFunction 页面功能信息
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody AdFunction adFunction) {
        return adFunctionService.updateById(adFunction);
    }

    /**
     * 查询所有页面功能信息。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public List<AdFunction> list() {
        return adFunctionService.list();
    }

    /**
     * 根据页面功能信息主键获取详细信息。
     *
     * @param id 页面功能信息主键
     * @return 页面功能信息详情
     */
    @GetMapping("getInfo/{id}")
    public AdFunction getInfo(@PathVariable Serializable id) {
        return adFunctionService.getById(id);
    }

    /**
     * 分页查询页面功能信息。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<AdFunction> page(Page<AdFunction> page) {
        return adFunctionService.page(page);
    }

}
