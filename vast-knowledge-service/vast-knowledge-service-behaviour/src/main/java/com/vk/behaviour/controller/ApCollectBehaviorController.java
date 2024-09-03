package com.vk.behaviour.controller;

import com.mybatisflex.core.paginate.Page;
import com.vk.behaviour.domain.ApCollectBehavior;
import com.vk.behaviour.service.ApCollectBehaviorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * APP收藏行为 控制层。
 *
 * @author 张三
 * @since 2024-09-03
 */
@RestController
@RequestMapping("/apCollectBehavior")
public class ApCollectBehaviorController {

    @Autowired
    private ApCollectBehaviorService apCollectBehaviorService;

    /**
     * 添加APP收藏行为。
     *
     * @param apCollectBehavior APP收藏行为
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody ApCollectBehavior apCollectBehavior) {
        return apCollectBehaviorService.save(apCollectBehavior);
    }

    /**
     * 根据主键删除APP收藏行为。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Serializable id) {
        return apCollectBehaviorService.removeById(id);
    }

    /**
     * 根据主键更新APP收藏行为。
     *
     * @param apCollectBehavior APP收藏行为
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody ApCollectBehavior apCollectBehavior) {
        return apCollectBehaviorService.updateById(apCollectBehavior);
    }

    /**
     * 查询所有APP收藏行为。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public List<ApCollectBehavior> list() {
        return apCollectBehaviorService.list();
    }

    /**
     * 根据APP收藏行为主键获取详细信息。
     *
     * @param id APP收藏行为主键
     * @return APP收藏行为详情
     */
    @GetMapping("getInfo/{id}")
    public ApCollectBehavior getInfo(@PathVariable Serializable id) {
        return apCollectBehaviorService.getById(id);
    }

    /**
     * 分页查询APP收藏行为。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<ApCollectBehavior> page(Page<ApCollectBehavior> page) {
        return apCollectBehaviorService.page(page);
    }

}
