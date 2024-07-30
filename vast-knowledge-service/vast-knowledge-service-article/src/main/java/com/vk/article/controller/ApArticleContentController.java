package com.vk.article.controller;

import com.mybatisflex.core.paginate.Page;
import com.vk.article.domain.ApArticleContent;
import com.vk.article.domain.dto.SaveArticleContentDto;
import com.vk.article.service.ApArticleContentService;
import com.vk.common.core.web.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

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
        return AjaxResult.success("保存完成",contentId);
    }

    @GetMapping("getInfo")
    public AjaxResult getInfoContent(@RequestParam(name = "id") Long id) {
        ApArticleContent vo =  apArticleContentService.getInfoContent(id);
        return AjaxResult.success(vo);
    }



}
