package com.vk.analyze.controller;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.vk.analyze.domain.AdSensitive;
import com.vk.analyze.service.AdSensitiveService;
import com.vk.common.core.domain.R;
import com.vk.common.core.exception.LeadNewsException;
import com.vk.common.redis.utlis.SensitiveWord;
import com.vk.common.core.utils.StringUtils;
import com.vk.common.core.web.domain.AjaxResult;
import com.vk.common.log.annotation.Log;
import com.vk.common.log.enums.BusinessType;
import com.vk.common.security.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

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

    @Autowired
    private SensitiveWord sensitiveWord;

    /**
     * 添加敏感词信息。
     *
     * @param adSensitive 敏感词信息
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    @RequiresPermissions("system:sensitive:add")
    @Log(title = "添加敏感词", businessType = BusinessType.INSERT)
    public AjaxResult save(@RequestBody AdSensitive adSensitive) {
        String sensitives = adSensitive.getSensitives();
        if (StringUtils.isEmpty(sensitives)){
            throw new LeadNewsException("敏感词不能为空");
        }
        AdSensitive addSensitive = new AdSensitive();
        addSensitive.setSensitives(sensitives);
        addSensitive.setType(adSensitive.getType());
        addSensitive.setCreatedTime(LocalDateTime.now());

        boolean save = adSensitiveService.save(addSensitive);
        if (save){
            sensitiveWord.addSensitive(Set.of(sensitives));
        }
        return AjaxResult.success();
    }

    /**
     * 根据主键删除敏感词信息。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    @RequiresPermissions("system:sensitive:del")
    @Log(title = "更新敏感词", businessType = BusinessType.DELETE)
    public AjaxResult remove(
            @PathVariable(name = "id") Serializable id
    ) {
        AdSensitive byId = adSensitiveService.getById(id);
        String sensitives = byId.getSensitives();
        if (ObjectUtils.isEmpty(byId)){
            throw new LeadNewsException("敏感词不存在,已经被移除");
        }
        boolean removeById = adSensitiveService.removeById(id);
        if (removeById){
            sensitiveWord.removeSensitive(Set.of(sensitives));
        }
        return AjaxResult.success() ;
    }

    /**
     * 根据主键更新敏感词信息。
     *
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    @RequiresPermissions("system:sensitive:up")
    @Log(title = "更新敏感词", businessType = BusinessType.UPDATE)
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
        String sensitives = byId.getSensitives();
        Boolean idType = byId.getType();
        if (sensitives.equals(name) && idType.equals(type)){
            throw new LeadNewsException("敏感词已经是："+(type?"不匹配":"匹配"));
        }
        AdSensitive adSensitive = new AdSensitive();
        adSensitive.setId(id);
        adSensitive.setSensitives(name);
        adSensitive.setType(type);
        boolean updateById = adSensitiveService.updateById(adSensitive);

        if (updateById){
            if (type){
                sensitiveWord.removeSensitive(Set.of(name));
            }else {
                sensitiveWord.upSensitive(sensitives,name);
            }
        }
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


    /**
     * 短文本处理
     * @param text
     * @return
     */
    @GetMapping("/sensitiveShort")
    public R<Map<String, AtomicInteger>> getSensitiveShort(
            @RequestParam(name = "text" ) String text
    ) {
        Map<String, AtomicInteger> stringAtomicIntegerMap = sensitiveWord.filterInfoShortText(text);
        return R.ok(stringAtomicIntegerMap);
    }

    /**
     * 文本处理
     * @param text
     * @return
     */
    @GetMapping("/sensitive")
    public R<Map<String, AtomicInteger>> getSensitive(
            @RequestParam(name = "text" ) String text
    ) {
        Map<String, AtomicInteger> stringAtomicIntegerMap = sensitiveWord.filterInfo(text);
        return R.ok(stringAtomicIntegerMap);
    }


}
