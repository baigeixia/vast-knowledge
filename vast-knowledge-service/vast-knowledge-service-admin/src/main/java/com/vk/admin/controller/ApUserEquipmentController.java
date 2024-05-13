package com.vk.admin.controller;

import com.mybatisflex.core.paginate.Page;
import com.vk.admin.domain.ApUserEquipment;
import com.vk.admin.service.ApUserEquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * APP用户设备信息 控制层。
 *
 * @author 张三
 * @since 2024-05-13
 */
@RestController
@RequestMapping("/apUserEquipment")
public class ApUserEquipmentController {

    @Autowired
    private ApUserEquipmentService apUserEquipmentService;

    /**
     * 添加APP用户设备信息。
     *
     * @param apUserEquipment APP用户设备信息
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody ApUserEquipment apUserEquipment) {
        return apUserEquipmentService.save(apUserEquipment);
    }

    /**
     * 根据主键删除APP用户设备信息。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Serializable id) {
        return apUserEquipmentService.removeById(id);
    }

    /**
     * 根据主键更新APP用户设备信息。
     *
     * @param apUserEquipment APP用户设备信息
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody ApUserEquipment apUserEquipment) {
        return apUserEquipmentService.updateById(apUserEquipment);
    }

    /**
     * 查询所有APP用户设备信息。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public List<ApUserEquipment> list() {
        return apUserEquipmentService.list();
    }

    /**
     * 根据APP用户设备信息主键获取详细信息。
     *
     * @param id APP用户设备信息主键
     * @return APP用户设备信息详情
     */
    @GetMapping("getInfo/{id}")
    public ApUserEquipment getInfo(@PathVariable Serializable id) {
        return apUserEquipmentService.getById(id);
    }

    /**
     * 分页查询APP用户设备信息。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<ApUserEquipment> page(Page<ApUserEquipment> page) {
        return apUserEquipmentService.page(page);
    }

}
