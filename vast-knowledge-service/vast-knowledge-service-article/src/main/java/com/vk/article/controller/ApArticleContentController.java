package com.vk.article.controller;

import com.vk.article.domain.ApArticleContent;
import com.vk.article.domain.dto.SaveArticleContentDto;
import com.vk.article.service.ApArticleContentService;
import com.vk.common.core.web.domain.AjaxResult;
import com.vk.common.log.annotation.Log;
import com.vk.common.log.enums.BusinessType;
import com.vk.common.security.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * APP已发布文章内容 控制层。
 *
 * @author 张三
 * @since 2024-07-11
 */
@RestController
@RequestMapping("/Content")
public class ApArticleContentController {

    @Autowired
    private ApArticleContentService apArticleContentService;


    @PostMapping("save")
    public AjaxResult saveContent(@RequestBody SaveArticleContentDto dto) {
        Long contentId = apArticleContentService.contentSave(dto);
        return AjaxResult.success("保存完成", contentId);
    }

    @GetMapping("getInfo")
    public AjaxResult getInfoContent(@RequestParam(name = "id") Long id) {
        ApArticleContent vo = apArticleContentService.getInfoContent(id);
        return AjaxResult.success(vo);
    }
    @GetMapping("Info")
    @RequiresPermissions("system:article:getInfo")
    public AjaxResult getBackstageInfoContent(@RequestParam(name = "id") Long id) {
        ApArticleContent vo = apArticleContentService.getInfoContent(id);
        return AjaxResult.success(vo);
    }


}
