package com.vk.analyze.controller;

import com.mybatisflex.core.paginate.Page;
import com.vk.analyze.domain.AdMenu;
import com.vk.analyze.service.AdMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * 菜单资源信息 控制层。
 *
 * @author 张三
 * @since 2024-05-13
 */
@RestController
@RequestMapping("/Menu")
public class AdMenuController {

    @Autowired
    private AdMenuService adMenuService;

    /**
     * 添加菜单资源信息。
     *
     * @param adMenu 菜单资源信息
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody AdMenu adMenu) {
        return adMenuService.save(adMenu);
    }

    /**
     * 根据主键删除菜单资源信息。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Serializable id) {
        return adMenuService.removeById(id);
    }

    /**
     * 根据主键更新菜单资源信息。
     *
     * @param adMenu 菜单资源信息
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody AdMenu adMenu) {
        return adMenuService.updateById(adMenu);
    }

    /**
     * 查询所有菜单资源信息。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public List<AdMenu> list() {
        return adMenuService.list();
    }

    /**
     * 根据菜单资源信息主键获取详细信息。
     *
     * @param id 菜单资源信息主键
     * @return 菜单资源信息详情
     */
    @GetMapping("getInfo/{id}")
    public AdMenu getInfo(@PathVariable Serializable id) {
        return adMenuService.getById(id);
    }

    /**
     * 分页查询菜单资源信息。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<AdMenu> page(Page<AdMenu> page) {
        return adMenuService.page(page);
    }

}
