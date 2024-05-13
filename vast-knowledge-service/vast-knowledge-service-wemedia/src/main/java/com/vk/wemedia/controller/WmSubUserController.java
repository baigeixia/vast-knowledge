package com.vk.wemedia.controller;

import com.mybatisflex.core.paginate.Page;
import com.vk.wemedia.domain.WmSubUser;
import com.vk.wemedia.service.WmSubUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * 自媒体子账号信息 控制层。
 *
 * @author 张三
 * @since 2024-05-13
 */
@RestController
@RequestMapping("/wmSubUser")
public class WmSubUserController {

    @Autowired
    private WmSubUserService wmSubUserService;

    /**
     * 添加自媒体子账号信息。
     *
     * @param wmSubUser 自媒体子账号信息
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody WmSubUser wmSubUser) {
        return wmSubUserService.save(wmSubUser);
    }

    /**
     * 根据主键删除自媒体子账号信息。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Serializable id) {
        return wmSubUserService.removeById(id);
    }

    /**
     * 根据主键更新自媒体子账号信息。
     *
     * @param wmSubUser 自媒体子账号信息
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody WmSubUser wmSubUser) {
        return wmSubUserService.updateById(wmSubUser);
    }

    /**
     * 查询所有自媒体子账号信息。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public List<WmSubUser> list() {
        return wmSubUserService.list();
    }

    /**
     * 根据自媒体子账号信息主键获取详细信息。
     *
     * @param id 自媒体子账号信息主键
     * @return 自媒体子账号信息详情
     */
    @GetMapping("getInfo/{id}")
    public WmSubUser getInfo(@PathVariable Serializable id) {
        return wmSubUserService.getById(id);
    }

    /**
     * 分页查询自媒体子账号信息。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<WmSubUser> page(Page<WmSubUser> page) {
        return wmSubUserService.page(page);
    }

}
