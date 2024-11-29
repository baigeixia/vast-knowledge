package com.vk.wemedia.controller;

import com.mybatisflex.core.paginate.Page;
import com.vk.common.core.exception.LeadNewsException;
import com.vk.common.core.web.domain.AjaxResult;
import com.vk.wemedia.domain.WmMaterial;
import com.vk.wemedia.domain.WmMaterialFeign;
import com.vk.wemedia.service.WmMaterialService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;

/**
 * 自媒体图文素材信息 控制层。
 *
 * @author 张三
 * @since 2024-05-13
 */
@RestController
@RequestMapping("/material")
public class WmMaterialController {

    @Autowired
    private WmMaterialService wmMaterialService;

    /**
     * 添加自媒体图文素材信息。
     *
     * @param wmMaterial 自媒体图文素材信息
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody WmMaterialFeign wmMaterial) {
        WmMaterial material = new WmMaterial();
        BeanUtils.copyProperties(wmMaterial, material);
        return wmMaterialService.save(material);
    }

    /**
     * 添加收藏。
     *
     * @param id 主键
     */
    @PutMapping("collection/{id}")
    public AjaxResult collection(@PathVariable(name = "id") Serializable id) {
        WmMaterial byId = wmMaterialService.getById(id);
        if (ObjectUtils.isEmpty(byId)){
            throw new LeadNewsException("图片错误 已经被移除");
        }
        WmMaterial material = new WmMaterial();
        material.setId(byId.getId());
        material.setIsCollection(!byId.getIsCollection());
        wmMaterialService.updateById(material);
        return AjaxResult.success();
    }

    /**
     * 根据主键删除自媒体图文素材信息。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public AjaxResult remove(@PathVariable(name = "id") Serializable id) {
        return AjaxResult.success(wmMaterialService.removeById(id));
    }


    /**
     * @param type 0全部 1收藏
     * @param page 页码
     * @param size 页数
     * @return
     */
    @GetMapping("page")
    public AjaxResult pageList(
            @RequestParam(name = "type") Integer type,
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "20") Integer size
    ) {
        Page<WmMaterial> result = wmMaterialService.pageList(type, page, size);
        return AjaxResult.success(result);
    }

}
