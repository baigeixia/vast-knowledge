package com.vk.wemedia.controller;

import com.mybatisflex.core.paginate.Page;
import com.vk.wemedia.domain.WmUser;
import com.vk.wemedia.service.WmUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * 自媒体用户信息 控制层。
 *
 * @author 张三
 * @since 2024-05-13
 */
@RestController
@RequestMapping("/User")
public class WmUserController {

    @Autowired
    private WmUserService wmUserService;

    /**
     * 添加自媒体用户信息。
     *
     * @param wmUser 自媒体用户信息
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody WmUser wmUser) {
        return wmUserService.save(wmUser);
    }

    /**
     * 根据主键删除自媒体用户信息。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Serializable id) {
        return wmUserService.removeById(id);
    }

    /**
     * 根据主键更新自媒体用户信息。
     *
     * @param wmUser 自媒体用户信息
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody WmUser wmUser) {
        return wmUserService.updateById(wmUser);
    }

    /**
     * 查询所有自媒体用户信息。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public List<WmUser> list() {
        return wmUserService.list();
    }

    /**
     * 根据自媒体用户信息主键获取详细信息。
     *
     * @param id 自媒体用户信息主键
     * @return 自媒体用户信息详情
     */
    @GetMapping("getInfo/{id}")
    public WmUser getInfo(@PathVariable Serializable id) {
        return wmUserService.getById(id);
    }

    /**
     * 分页查询自媒体用户信息。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<WmUser> page(Page<WmUser> page) {
        return wmUserService.page(page);
    }

}
