package com.vk.behaviour.controller;

import com.mybatisflex.core.paginate.Page;
import com.vk.behaviour.domain.ApUnlikesBehavior;
import com.vk.behaviour.service.ApUnlikesBehaviorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * APP不喜欢行为 控制层。
 *
 * @author 张三
 * @since 2024-05-13
 */
@RestController
@RequestMapping("/apUnlikesBehavior")
public class ApUnlikesBehaviorController {

    @Autowired
    private ApUnlikesBehaviorService apUnlikesBehaviorService;

    /**
     * 添加APP不喜欢行为。
     *
     * @param apUnlikesBehavior APP不喜欢行为
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody ApUnlikesBehavior apUnlikesBehavior) {
        return apUnlikesBehaviorService.save(apUnlikesBehavior);
    }

    /**
     * 根据主键删除APP不喜欢行为。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Serializable id) {
        return apUnlikesBehaviorService.removeById(id);
    }

    /**
     * 根据主键更新APP不喜欢行为。
     *
     * @param apUnlikesBehavior APP不喜欢行为
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody ApUnlikesBehavior apUnlikesBehavior) {
        return apUnlikesBehaviorService.updateById(apUnlikesBehavior);
    }

    /**
     * 查询所有APP不喜欢行为。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public List<ApUnlikesBehavior> list() {
        return apUnlikesBehaviorService.list();
    }

    /**
     * 根据APP不喜欢行为主键获取详细信息。
     *
     * @param id APP不喜欢行为主键
     * @return APP不喜欢行为详情
     */
    @GetMapping("getInfo/{id}")
    public ApUnlikesBehavior getInfo(@PathVariable Serializable id) {
        return apUnlikesBehaviorService.getById(id);
    }

    /**
     * 分页查询APP不喜欢行为。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<ApUnlikesBehavior> page(Page<ApUnlikesBehavior> page) {
        return apUnlikesBehaviorService.page(page);
    }

}
