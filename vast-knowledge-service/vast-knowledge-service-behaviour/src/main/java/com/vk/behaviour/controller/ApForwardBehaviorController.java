package com.vk.behaviour.controller;

import com.mybatisflex.core.paginate.Page;
import com.vk.behaviour.domain.ApForwardBehavior;
import com.vk.behaviour.service.ApForwardBehaviorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * APP转发行为 控制层。
 *
 * @author 张三
 * @since 2024-05-13
 */
@RestController
@RequestMapping("/apForwardBehavior")
public class ApForwardBehaviorController {

    @Autowired
    private ApForwardBehaviorService apForwardBehaviorService;

    /**
     * 添加APP转发行为。
     *
     * @param apForwardBehavior APP转发行为
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody ApForwardBehavior apForwardBehavior) {
        return apForwardBehaviorService.save(apForwardBehavior);
    }

    /**
     * 根据主键删除APP转发行为。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Serializable id) {
        return apForwardBehaviorService.removeById(id);
    }

    /**
     * 根据主键更新APP转发行为。
     *
     * @param apForwardBehavior APP转发行为
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody ApForwardBehavior apForwardBehavior) {
        return apForwardBehaviorService.updateById(apForwardBehavior);
    }

    /**
     * 查询所有APP转发行为。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public List<ApForwardBehavior> list() {
        return apForwardBehaviorService.list();
    }

    /**
     * 根据APP转发行为主键获取详细信息。
     *
     * @param id APP转发行为主键
     * @return APP转发行为详情
     */
    @GetMapping("getInfo/{id}")
    public ApForwardBehavior getInfo(@PathVariable Serializable id) {
        return apForwardBehaviorService.getById(id);
    }

    /**
     * 分页查询APP转发行为。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<ApForwardBehavior> page(Page<ApForwardBehavior> page) {
        return apForwardBehaviorService.page(page);
    }

}
