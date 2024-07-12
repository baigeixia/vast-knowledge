package com.vk.user.controller;

import com.mybatisflex.core.paginate.Page;
import com.vk.user.domain.ApUserFan;
import com.vk.user.service.ApUserFanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * APP用户粉丝信息 控制层。
 *
 * @author 张三
 * @since 2024-05-13
 */
@RestController
@RequestMapping("/apUserFan")
public class ApUserFanController {

    @Autowired
    private ApUserFanService apUserFanService;

    /**
     * 添加APP用户粉丝信息。
     *
     * @param apUserFan APP用户粉丝信息
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody ApUserFan apUserFan) {
        return apUserFanService.save(apUserFan);
    }

    /**
     * 根据主键删除APP用户粉丝信息。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Serializable id) {
        return apUserFanService.removeById(id);
    }

    /**
     * 根据主键更新APP用户粉丝信息。
     *
     * @param apUserFan APP用户粉丝信息
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody ApUserFan apUserFan) {
        return apUserFanService.updateById(apUserFan);
    }

    /**
     * 查询所有APP用户粉丝信息。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public List<ApUserFan> list() {
        return apUserFanService.list();
    }

    /**
     * 根据APP用户粉丝信息主键获取详细信息。
     *
     * @param id APP用户粉丝信息主键
     * @return APP用户粉丝信息详情
     */
    @GetMapping("getInfo/{id}")
    public ApUserFan getInfo(@PathVariable Serializable id) {
        return apUserFanService.getById(id);
    }

    /**
     * 分页查询APP用户粉丝信息。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<ApUserFan> page(Page<ApUserFan> page) {
        return apUserFanService.page(page);
    }

}
