package com.vk.behaviour.controller;

import com.mybatisflex.core.paginate.Page;
import com.vk.behaviour.domain.ApShareBehavior;
import com.vk.behaviour.service.ApShareBehaviorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * APP分享行为 控制层。
 *
 * @author 张三
 * @since 2024-05-13
 */
@RestController
@RequestMapping("/ShareBehavior")
public class ApShareBehaviorController {

    @Autowired
    private ApShareBehaviorService apShareBehaviorService;

    /**
     * 添加APP分享行为。
     *
     * @param apShareBehavior APP分享行为
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody ApShareBehavior apShareBehavior) {
        return apShareBehaviorService.save(apShareBehavior);
    }

    /**
     * 根据主键删除APP分享行为。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Serializable id) {
        return apShareBehaviorService.removeById(id);
    }

    /**
     * 根据主键更新APP分享行为。
     *
     * @param apShareBehavior APP分享行为
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody ApShareBehavior apShareBehavior) {
        return apShareBehaviorService.updateById(apShareBehavior);
    }

    /**
     * 查询所有APP分享行为。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public List<ApShareBehavior> list() {
        return apShareBehaviorService.list();
    }

    /**
     * 根据APP分享行为主键获取详细信息。
     *
     * @param id APP分享行为主键
     * @return APP分享行为详情
     */
    @GetMapping("getInfo/{id}")
    public ApShareBehavior getInfo(@PathVariable Serializable id) {
        return apShareBehaviorService.getById(id);
    }

    /**
     * 分页查询APP分享行为。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<ApShareBehavior> page(Page<ApShareBehavior> page) {
        return apShareBehaviorService.page(page);
    }

}
