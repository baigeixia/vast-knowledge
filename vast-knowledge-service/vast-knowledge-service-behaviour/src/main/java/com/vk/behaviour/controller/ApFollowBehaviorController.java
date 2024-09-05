package com.vk.behaviour.controller;

import com.mybatisflex.core.paginate.Page;
import com.vk.behaviour.domain.ApFollowBehavior;
import com.vk.behaviour.service.ApFollowBehaviorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * APP关注行为 控制层。
 *
 * @author 张三
 * @since 2024-05-13
 */
@RestController
@RequestMapping("/follow")
public class ApFollowBehaviorController {

    @Autowired
    private ApFollowBehaviorService apFollowBehaviorService;

    /**
     * 添加APP关注行为。
     *
     * @param apFollowBehavior APP关注行为
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody ApFollowBehavior apFollowBehavior) {
        return apFollowBehaviorService.save(apFollowBehavior);
    }

    /**
     * 根据主键删除APP关注行为。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Serializable id) {
        return apFollowBehaviorService.removeById(id);
    }

    /**
     * 根据主键更新APP关注行为。
     *
     * @param apFollowBehavior APP关注行为
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody ApFollowBehavior apFollowBehavior) {
        return apFollowBehaviorService.updateById(apFollowBehavior);
    }

    /**
     * 查询所有APP关注行为。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public List<ApFollowBehavior> list() {
        return apFollowBehaviorService.list();
    }

    /**
     * 根据APP关注行为主键获取详细信息。
     *
     * @param id APP关注行为主键
     * @return APP关注行为详情
     */
    @GetMapping("getInfo/{id}")
    public ApFollowBehavior getInfo(@PathVariable Serializable id) {
        return apFollowBehaviorService.getById(id);
    }

    /**
     * 分页查询APP关注行为。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<ApFollowBehavior> page(Page<ApFollowBehavior> page) {
        return apFollowBehaviorService.page(page);
    }

}
