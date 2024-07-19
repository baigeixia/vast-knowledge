package com.vk.behaviour.controller;

import com.mybatisflex.core.paginate.Page;
import com.vk.behaviour.domain.ApBehaviorEntry;
import com.vk.behaviour.service.ApBehaviorEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * APP行为实体,一个行为实体可能是用户或者设备，或者其它 控制层。
 *
 * @author 张三
 * @since 2024-05-13
 */
@RestController
@RequestMapping("/BehaviorEntry")
public class ApBehaviorEntryController {

    @Autowired
    private ApBehaviorEntryService apBehaviorEntryService;

    /**
     * 添加APP行为实体,一个行为实体可能是用户或者设备，或者其它。
     *
     * @param apBehaviorEntry APP行为实体,一个行为实体可能是用户或者设备，或者其它
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody ApBehaviorEntry apBehaviorEntry) {
        return apBehaviorEntryService.save(apBehaviorEntry);
    }

    /**
     * 根据主键删除APP行为实体,一个行为实体可能是用户或者设备，或者其它。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Serializable id) {
        return apBehaviorEntryService.removeById(id);
    }

    /**
     * 根据主键更新APP行为实体,一个行为实体可能是用户或者设备，或者其它。
     *
     * @param apBehaviorEntry APP行为实体,一个行为实体可能是用户或者设备，或者其它
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody ApBehaviorEntry apBehaviorEntry) {
        return apBehaviorEntryService.updateById(apBehaviorEntry);
    }

    /**
     * 查询所有APP行为实体,一个行为实体可能是用户或者设备，或者其它。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public List<ApBehaviorEntry> list() {
        return apBehaviorEntryService.list();
    }

    /**
     * 根据APP行为实体,一个行为实体可能是用户或者设备，或者其它主键获取详细信息。
     *
     * @param id APP行为实体,一个行为实体可能是用户或者设备，或者其它主键
     * @return APP行为实体,一个行为实体可能是用户或者设备，或者其它详情
     */
    @GetMapping("getInfo/{id}")
    public ApBehaviorEntry getInfo(@PathVariable Serializable id) {
        return apBehaviorEntryService.getById(id);
    }

    /**
     * 分页查询APP行为实体,一个行为实体可能是用户或者设备，或者其它。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<ApBehaviorEntry> page(Page<ApBehaviorEntry> page) {
        return apBehaviorEntryService.page(page);
    }

}
