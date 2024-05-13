package com.vk.analyze.controller;

import com.mybatisflex.core.paginate.Page;
import com.vk.analyze.domain.AdSensitive;
import com.vk.analyze.service.AdSensitiveService;
import com.vk.common.core.web.controller.BaseController;
import com.vk.common.core.web.domain.AjaxResult;
import com.vk.common.core.web.page.TableDataInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * 敏感词信息 控制层。
 *
 * @author 张三
 * @since 2024-05-13
 */
@RestController
@RequestMapping("/adSensitive")
public class AdSensitiveController   {

    @Autowired
    private AdSensitiveService adSensitiveService;

    /**
     * 添加敏感词信息。
     *
     * @param adSensitive 敏感词信息
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody AdSensitive adSensitive) {
        return adSensitiveService.save(adSensitive);
    }

    /**
     * 根据主键删除敏感词信息。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Serializable id) {
        return adSensitiveService.removeById(id);
    }

    /**
     * 根据主键更新敏感词信息。
     *
     * @param adSensitive 敏感词信息
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody AdSensitive adSensitive) {
        return adSensitiveService.updateById(adSensitive);
    }

    /**
     * 查询所有敏感词信息。
     *
     * @return 所有数据
     */
    @PostMapping("list")
    public AjaxResult getlist(@RequestBody AdSensitive adSensitive) {
        List<AdSensitive> list= adSensitiveService.getlist(adSensitive);
        return AjaxResult.success(list);
    }

    /**
     * 根据敏感词信息主键获取详细信息。
     *
     * @param id 敏感词信息主键
     * @return 敏感词信息详情
     */
    @GetMapping("getInfo/{id}")
    public AdSensitive getInfo(@PathVariable Serializable id) {
        return adSensitiveService.getById(id);
    }

    /**
     * 分页查询敏感词信息。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<AdSensitive> page(Page<AdSensitive> page) {
        return adSensitiveService.page(page);
    }

}
