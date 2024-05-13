package com.vk.admin.controller;

import com.mybatisflex.core.paginate.Page;
import com.vk.admin.domain.ApUser;
import com.vk.admin.service.ApUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * APP用户信息 控制层。
 *
 * @author 张三
 * @since 2024-05-13
 */
@RestController
@RequestMapping("/apUser")
public class ApUserController {

    @Autowired
    private ApUserService apUserService;

    /**
     * 添加APP用户信息。
     *
     * @param apUser APP用户信息
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody ApUser apUser) {
        return apUserService.save(apUser);
    }

    /**
     * 根据主键删除APP用户信息。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Serializable id) {
        return apUserService.removeById(id);
    }

    /**
     * 根据主键更新APP用户信息。
     *
     * @param apUser APP用户信息
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody ApUser apUser) {
        return apUserService.updateById(apUser);
    }

    /**
     * 查询所有APP用户信息。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public List<ApUser> list() {
        return apUserService.list();
    }

    /**
     * 根据APP用户信息主键获取详细信息。
     *
     * @param id APP用户信息主键
     * @return APP用户信息详情
     */
    @GetMapping("getInfo/{id}")
    public ApUser getInfo(@PathVariable Serializable id) {
        return apUserService.getById(id);
    }

    /**
     * 分页查询APP用户信息。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<ApUser> page(Page<ApUser> page) {
        return apUserService.page(page);
    }

}
