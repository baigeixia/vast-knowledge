package com.vk.analyze.controller;

import com.mybatisflex.core.paginate.Page;
import com.vk.analyze.domain.AdStrategyGroup;
import com.vk.analyze.service.AdStrategyGroupService;
import com.vk.common.core.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * 推荐策略分组信息 控制层。
 *
 * @author 张三
 * @since 2024-05-13
 */
@RestController
@RequestMapping("/StrategyGroup")
public class AdStrategyGroupController {

    @Autowired
    private AdStrategyGroupService adStrategyGroupService;

    /**
     * 添加推荐策略分组信息。
     *
     * @param adStrategyGroup 推荐策略分组信息
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody AdStrategyGroup adStrategyGroup) {
        return adStrategyGroupService.save(adStrategyGroup);
    }

    /**
     * 根据主键删除推荐策略分组信息。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Serializable id) {
        return adStrategyGroupService.removeById(id);
    }

    /**
     * 根据主键更新推荐策略分组信息。
     *
     * @param adStrategyGroup 推荐策略分组信息
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody AdStrategyGroup adStrategyGroup) {
        return adStrategyGroupService.updateById(adStrategyGroup);
    }

    /**
     * 查询所有推荐策略分组信息。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public List<AdStrategyGroup> list() {

        return adStrategyGroupService.list();
    }

    /**
     * 根据推荐策略分组信息主键获取详细信息。
     *
     * @param id 推荐策略分组信息主键
     * @return 推荐策略分组信息详情
     */
    @GetMapping("getInfo/{id}")
    public AdStrategyGroup getInfo(@PathVariable Serializable id) {
        return adStrategyGroupService.getById(id);
    }

    /**
     * 分页查询推荐策略分组信息。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<AdStrategyGroup> page(Page<AdStrategyGroup> page) {
        return adStrategyGroupService.page(page);
    }

}
