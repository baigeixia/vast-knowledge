package com.vk.admin.controller;

import com.mybatisflex.core.paginate.Page;
import com.vk.admin.domain.ApUserInfo;
import com.vk.admin.service.ApUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * APP用户详情信息 控制层。
 *
 * @author 张三
 * @since 2024-05-13
 */
@RestController
@RequestMapping("/apUserInfo")
public class ApUserInfoController {

    @Autowired
    private ApUserInfoService apUserInfoService;

    /**
     * 添加APP用户详情信息。
     *
     * @param apUserInfo APP用户详情信息
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody ApUserInfo apUserInfo) {
        return apUserInfoService.save(apUserInfo);
    }

    /**
     * 根据主键删除APP用户详情信息。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Serializable id) {
        return apUserInfoService.removeById(id);
    }

    /**
     * 根据主键更新APP用户详情信息。
     *
     * @param apUserInfo APP用户详情信息
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody ApUserInfo apUserInfo) {
        return apUserInfoService.updateById(apUserInfo);
    }

    /**
     * 查询所有APP用户详情信息。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public List<ApUserInfo> list() {
        return apUserInfoService.list();
    }

    /**
     * 根据APP用户详情信息主键获取详细信息。
     *
     * @param id APP用户详情信息主键
     * @return APP用户详情信息详情
     */
    @GetMapping("getInfo/{id}")
    public ApUserInfo getInfo(@PathVariable Serializable id) {
        return apUserInfoService.getById(id);
    }

    /**
     * 分页查询APP用户详情信息。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<ApUserInfo> page(Page<ApUserInfo> page) {
        return apUserInfoService.page(page);
    }

}
