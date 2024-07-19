package com.vk.article.controller;

import com.mybatisflex.core.paginate.Page;
import com.vk.article.domain.ApEquipmentCode;
import com.vk.article.service.ApEquipmentCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * APP设备码信息 控制层。
 *
 * @author 张三
 * @since 2024-07-11
 */
@RestController
@RequestMapping("/EquipmentCode")
public class ApEquipmentCodeController {

    @Autowired
    private ApEquipmentCodeService apEquipmentCodeService;

    /**
     * 添加APP设备码信息。
     *
     * @param apEquipmentCode APP设备码信息
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody ApEquipmentCode apEquipmentCode) {
        return apEquipmentCodeService.save(apEquipmentCode);
    }

    /**
     * 根据主键删除APP设备码信息。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Serializable id) {
        return apEquipmentCodeService.removeById(id);
    }

    /**
     * 根据主键更新APP设备码信息。
     *
     * @param apEquipmentCode APP设备码信息
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody ApEquipmentCode apEquipmentCode) {
        return apEquipmentCodeService.updateById(apEquipmentCode);
    }

    /**
     * 查询所有APP设备码信息。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public List<ApEquipmentCode> list() {
        return apEquipmentCodeService.list();
    }

    /**
     * 根据APP设备码信息主键获取详细信息。
     *
     * @param id APP设备码信息主键
     * @return APP设备码信息详情
     */
    @GetMapping("getInfo/{id}")
    public ApEquipmentCode getInfo(@PathVariable Serializable id) {
        return apEquipmentCodeService.getById(id);
    }

    /**
     * 分页查询APP设备码信息。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<ApEquipmentCode> page(Page<ApEquipmentCode> page) {
        return apEquipmentCodeService.page(page);
    }

}
