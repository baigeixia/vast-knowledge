package com.vk.behaviour.controller;

import com.mybatisflex.core.paginate.Page;
import com.vk.behaviour.domain.ApShowBehavior;
import com.vk.behaviour.service.ApShowBehaviorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * APP文章展现行为 控制层。
 *
 * @author 张三
 * @since 2024-05-13
 */
@RestController
@RequestMapping("/ShowBehavior")
public class ApShowBehaviorController {

    @Autowired
    private ApShowBehaviorService apShowBehaviorService;

    /**
     * 添加APP文章展现行为。
     *
     * @param apShowBehavior APP文章展现行为
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody ApShowBehavior apShowBehavior) {
        return apShowBehaviorService.save(apShowBehavior);
    }

    /**
     * 根据主键删除APP文章展现行为。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Serializable id) {
        return apShowBehaviorService.removeById(id);
    }

    /**
     * 根据主键更新APP文章展现行为。
     *
     * @param apShowBehavior APP文章展现行为
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody ApShowBehavior apShowBehavior) {
        return apShowBehaviorService.updateById(apShowBehavior);
    }

    /**
     * 查询所有APP文章展现行为。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public List<ApShowBehavior> list() {
        return apShowBehaviorService.list();
    }

    /**
     * 根据APP文章展现行为主键获取详细信息。
     *
     * @param id APP文章展现行为主键
     * @return APP文章展现行为详情
     */
    @GetMapping("getInfo/{id}")
    public ApShowBehavior getInfo(@PathVariable Serializable id) {
        return apShowBehaviorService.getById(id);
    }

    /**
     * 分页查询APP文章展现行为。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<ApShowBehavior> page(Page<ApShowBehavior> page) {
        return apShowBehaviorService.page(page);
    }

}
