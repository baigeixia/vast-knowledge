package com.vk.admin.controller;

import com.mybatisflex.core.paginate.Page;
import com.vk.admin.domain.ApUserDynamicList;
import com.vk.admin.service.ApUserDynamicListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * APP用户动态列 控制层。
 *
 * @author 张三
 * @since 2024-05-13
 */
@RestController
@RequestMapping("/apUserDynamicList")
public class ApUserDynamicListController {

    @Autowired
    private ApUserDynamicListService apUserDynamicListService;

    /**
     * 添加APP用户动态列。
     *
     * @param apUserDynamicList APP用户动态列
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody ApUserDynamicList apUserDynamicList) {
        return apUserDynamicListService.save(apUserDynamicList);
    }

    /**
     * 根据主键删除APP用户动态列。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Serializable id) {
        return apUserDynamicListService.removeById(id);
    }

    /**
     * 根据主键更新APP用户动态列。
     *
     * @param apUserDynamicList APP用户动态列
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody ApUserDynamicList apUserDynamicList) {
        return apUserDynamicListService.updateById(apUserDynamicList);
    }

    /**
     * 查询所有APP用户动态列。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public List<ApUserDynamicList> list() {
        return apUserDynamicListService.list();
    }

    /**
     * 根据APP用户动态列主键获取详细信息。
     *
     * @param id APP用户动态列主键
     * @return APP用户动态列详情
     */
    @GetMapping("getInfo/{id}")
    public ApUserDynamicList getInfo(@PathVariable Serializable id) {
        return apUserDynamicListService.getById(id);
    }

    /**
     * 分页查询APP用户动态列。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<ApUserDynamicList> page(Page<ApUserDynamicList> page) {
        return apUserDynamicListService.page(page);
    }

}
