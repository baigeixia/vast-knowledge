package com.vk.user.controller;

import com.mybatisflex.core.paginate.Page;
import com.vk.user.domain.ApUserFollow;
import com.vk.user.service.ApUserFollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * APP用户关注信息 控制层。
 *
 * @author 张三
 * @since 2024-05-13
 */
@RestController
@RequestMapping("/apUserFollow")
public class ApUserFollowController {

    @Autowired
    private ApUserFollowService apUserFollowService;

    /**
     * 添加APP用户关注信息。
     *
     * @param apUserFollow APP用户关注信息
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody ApUserFollow apUserFollow) {
        return apUserFollowService.save(apUserFollow);
    }

    /**
     * 根据主键删除APP用户关注信息。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Serializable id) {
        return apUserFollowService.removeById(id);
    }

    /**
     * 根据主键更新APP用户关注信息。
     *
     * @param apUserFollow APP用户关注信息
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody ApUserFollow apUserFollow) {
        return apUserFollowService.updateById(apUserFollow);
    }

    /**
     * 查询所有APP用户关注信息。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public List<ApUserFollow> list() {
        return apUserFollowService.list();
    }

    /**
     * 根据APP用户关注信息主键获取详细信息。
     *
     * @param id APP用户关注信息主键
     * @return APP用户关注信息详情
     */
    @GetMapping("getInfo/{id}")
    public ApUserFollow getInfo(@PathVariable Serializable id) {
        return apUserFollowService.getById(id);
    }

    /**
     * 分页查询APP用户关注信息。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<ApUserFollow> page(Page<ApUserFollow> page) {
        return apUserFollowService.page(page);
    }

}
