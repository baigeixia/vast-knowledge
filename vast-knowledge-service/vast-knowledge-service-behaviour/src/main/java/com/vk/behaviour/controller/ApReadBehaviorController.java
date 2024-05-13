package com.vk.behaviour.controller;

import com.mybatisflex.core.paginate.Page;
import com.vk.behaviour.domain.ApReadBehavior;
import com.vk.behaviour.service.ApReadBehaviorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * APP阅读行为 控制层。
 *
 * @author 张三
 * @since 2024-05-13
 */
@RestController
@RequestMapping("/apReadBehavior")
public class ApReadBehaviorController {

    @Autowired
    private ApReadBehaviorService apReadBehaviorService;

    /**
     * 添加APP阅读行为。
     *
     * @param apReadBehavior APP阅读行为
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody ApReadBehavior apReadBehavior) {
        return apReadBehaviorService.save(apReadBehavior);
    }

    /**
     * 根据主键删除APP阅读行为。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Serializable id) {
        return apReadBehaviorService.removeById(id);
    }

    /**
     * 根据主键更新APP阅读行为。
     *
     * @param apReadBehavior APP阅读行为
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody ApReadBehavior apReadBehavior) {
        return apReadBehaviorService.updateById(apReadBehavior);
    }

    /**
     * 查询所有APP阅读行为。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public List<ApReadBehavior> list() {
        return apReadBehaviorService.list();
    }

    /**
     * 根据APP阅读行为主键获取详细信息。
     *
     * @param id APP阅读行为主键
     * @return APP阅读行为详情
     */
    @GetMapping("getInfo/{id}")
    public ApReadBehavior getInfo(@PathVariable Serializable id) {
        return apReadBehaviorService.getById(id);
    }

    /**
     * 分页查询APP阅读行为。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<ApReadBehavior> page(Page<ApReadBehavior> page) {
        return apReadBehaviorService.page(page);
    }

}
