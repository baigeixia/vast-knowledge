package com.vk.behaviour.controller;

import com.mybatisflex.core.paginate.Page;
import com.vk.behaviour.domain.ApLikesBehavior;
import com.vk.behaviour.service.ApLikesBehaviorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * APP点赞行为 控制层。
 *
 * @author 张三
 * @since 2024-05-13
 */
@RestController
@RequestMapping("/LikesBehavior")
public class ApLikesBehaviorController {

    @Autowired
    private ApLikesBehaviorService apLikesBehaviorService;

    /**
     * 添加APP点赞行为。
     *
     * @param apLikesBehavior APP点赞行为
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody ApLikesBehavior apLikesBehavior) {
        return apLikesBehaviorService.save(apLikesBehavior);
    }

    /**
     * 根据主键删除APP点赞行为。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Serializable id) {
        return apLikesBehaviorService.removeById(id);
    }

    /**
     * 根据主键更新APP点赞行为。
     *
     * @param apLikesBehavior APP点赞行为
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody ApLikesBehavior apLikesBehavior) {
        return apLikesBehaviorService.updateById(apLikesBehavior);
    }

    /**
     * 查询所有APP点赞行为。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public List<ApLikesBehavior> list() {
        return apLikesBehaviorService.list();
    }

    /**
     * 根据APP点赞行为主键获取详细信息。
     *
     * @param id APP点赞行为主键
     * @return APP点赞行为详情
     */
    @GetMapping("getInfo/{id}")
    public ApLikesBehavior getInfo(@PathVariable Serializable id) {
        return apLikesBehaviorService.getById(id);
    }

    /**
     * 分页查询APP点赞行为。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<ApLikesBehavior> page(Page<ApLikesBehavior> page) {
        return apLikesBehaviorService.page(page);
    }

}
