package com.vk.user.controller;


import com.mybatisflex.core.paginate.Page;
import com.vk.user.domain.ApUserRealname;
import com.vk.user.domain.dto.ApUserRealnameDto;
import com.vk.user.service.ApUserRealnameService;
import com.vk.common.core.web.controller.BaseController;
import com.vk.common.core.web.domain.AjaxResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;

/**
 * @description <p>APP实名认证信息</p>
 *
 * @version 1.0
 * @package com.vk.user.controller
 */
@RestController
@RequestMapping("/auth")
public class ApUserRealnameController extends BaseController {


    @Autowired
    private ApUserRealnameService apUserRealnameService;

    /**
     * 添加APP实名认证信息。
     *
     * @param apUserRealname APP实名认证信息
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody ApUserRealname apUserRealname) {
        return apUserRealnameService.save(apUserRealname);
    }

    /**
     * 根据主键删除APP实名认证信息。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Serializable id) {
        return apUserRealnameService.removeById(id);
    }

    /**
     * 根据主键更新APP实名认证信息。
     *
     * @param apUserRealname APP实名认证信息
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody ApUserRealname apUserRealname) {
        return apUserRealnameService.updateById(apUserRealname);
    }

    /**
     * 查询所有APP实名认证信息。
     *
     * @return 所有数据
     */
    @PostMapping("list")
    public AjaxResult list(@RequestBody ApUserRealnameDto dto) {
        Page<ApUserRealname> list= apUserRealnameService.getlist(dto);
        return AjaxResult.success(list);
    }

    /**
     * 根据APP实名认证信息主键获取详细信息。
     *
     * @param id APP实名认证信息主键
     * @return APP实名认证信息详情
     */
    @GetMapping("getInfo/{id}")
    public ApUserRealname getInfo(@PathVariable Serializable id) {
        return apUserRealnameService.getById(id);
    }

    /**
     * 分页查询APP实名认证信息。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<ApUserRealname> page(Page<ApUserRealname> page) {
        return apUserRealnameService.page(page);
    }


}

