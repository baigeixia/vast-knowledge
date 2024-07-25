package com.vk.analyze.controller;

import com.mybatisflex.core.paginate.Page;
import com.vk.analyze.domain.AdLabel;
import com.vk.analyze.service.AdLabelService;
import com.vk.common.core.web.controller.BaseController;
import com.vk.common.core.web.domain.AjaxResult;
import com.vk.common.core.web.page.TableDataInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * 标签信息 控制层。
 *
 * @author 张三
 * @since 2024-05-13
 */
@RestController
@RequestMapping("/label")
public class AdLabelController  extends BaseController {

    @Autowired
    private AdLabelService adLabelService;

    /**
     * 添加标签信息。
     *
     * @param adLabel 标签信息
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody AdLabel adLabel) {
        return adLabelService.save(adLabel);
    }

    /**
     * 根据主键删除标签信息。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Serializable id) {
        return adLabelService.removeById(id);
    }

    /**
     * 根据主键更新标签信息。
     *
     * @param adLabel 标签信息
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody AdLabel adLabel) {
        return adLabelService.updateById(adLabel);
    }

    /**
     * 查询所有标签信息。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public AjaxResult list() {
        List<AdLabel> list = adLabelService.list();
        return AjaxResult.success(list);
    }

    /**
     * 根据标签信息主键获取详细信息。
     *
     * @param id 标签信息主键
     * @return 标签信息详情
     */
    @GetMapping("getInfo/{id}")
    public AdLabel getInfo(@PathVariable Serializable id) {
        return adLabelService.getById(id);
    }

    /**
     * 分页查询标签信息。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<AdLabel> page(Page<AdLabel> page) {
        return adLabelService.page(page);
    }

}
