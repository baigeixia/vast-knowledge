package com.vk.analyze.controller;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.vk.analyze.domain.AdSensitive;
import com.vk.analyze.domain.table.AdSensitiveTableDef;
import com.vk.analyze.service.AdSensitiveService;
import com.vk.common.core.exception.LeadNewsException;
import com.vk.common.core.utils.StringUtils;
import com.vk.common.core.web.controller.BaseController;
import com.vk.common.core.web.domain.AjaxResult;
import com.vk.common.core.web.page.TableDataInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import static com.vk.analyze.domain.table.AdSensitiveTableDef.AD_SENSITIVE;

/**
 * 敏感词信息 控制层。
 *
 * @author 张三
 * @since 2024-05-13
 */
@RestController
@RequestMapping("/sensitive")
public class AdSensitiveController   {

    @Autowired
    private AdSensitiveService adSensitiveService;

    /**
     * 添加敏感词信息。
     *
     * @param adSensitive 敏感词信息
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public AjaxResult save(@RequestBody AdSensitive adSensitive) {
        String sensitives = adSensitive.getSensitives();
        if (StringUtils.isEmpty(sensitives)){
            throw new LeadNewsException("敏感词不能为空");
        }

        AdSensitive addSensitive = new AdSensitive();
        addSensitive.setSensitives(adSensitive.getSensitives());
        addSensitive.setType(adSensitive.getType());
        addSensitive.setCreatedTime(LocalDateTime.now());

        adSensitiveService.save(addSensitive);
        return AjaxResult.success();
    }

    /**
     * 根据主键删除敏感词信息。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public AjaxResult remove(
            @PathVariable(name = "id") Serializable id
    ) {
        AdSensitive byId = adSensitiveService.getById(id);
        if (ObjectUtils.isEmpty(byId)){
            throw new LeadNewsException("敏感词不存在,已经被移除");
        }
        return AjaxResult.success(adSensitiveService.removeById(id)) ;
    }

    /**
     * 根据主键更新敏感词信息。
     *
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public AjaxResult update(
            @RequestParam(name = "id") Long id,
            @RequestParam(name = "name") String name,
            @RequestParam(name = "type") Boolean type
    ) {
        if (StringUtils.isEmpty(name)){
            throw new LeadNewsException("敏感词修改不能为空");
        }
        AdSensitive byId = adSensitiveService.getById(id);
        if (ObjectUtils.isEmpty(byId)){
            throw new LeadNewsException("敏感词不存在,已经被移除");
        }
        AdSensitive adSensitive = new AdSensitive();
        adSensitive.setId(id);
        adSensitive.setSensitives(name);
        adSensitive.setType(type);
        adSensitiveService.updateById(adSensitive);
        return AjaxResult.success();
    }

    /**
     * 查询所有敏感词信息。
     *
     * @return 所有数据
     */
    @PostMapping("list")
    public AjaxResult getList(
            @RequestParam(name = "page",defaultValue = "1",required = false) Long page ,
            @RequestParam(name = "size" ,defaultValue = "10",required = false) Long size,
            @RequestParam(name = "name" ,required = false) String name
    ) {
        Page<AdSensitive> paged = adSensitiveService.page(Page.of(page, size),
                QueryWrapper.create().where(AD_SENSITIVE.SENSITIVES.like(name, StringUtils.isNotEmpty(name)))
                        .orderBy(AD_SENSITIVE.CREATED_TIME,false));
        return AjaxResult.success(paged);
    }


}
