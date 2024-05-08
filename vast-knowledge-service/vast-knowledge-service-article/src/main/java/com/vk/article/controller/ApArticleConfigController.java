package com.vk.article.controller;



import com.vk.article.domain.ApArticleConfig;
import com.vk.article.service.ApArticleConfigService;
import com.vk.common.core.web.domain.AjaxResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description <p>APP已发布文章配置</p>
 *
 * @version 1.0
 * @package com.vk.article.controller
 */
@RestController
@RequestMapping("/apArticleConfig")
@Tag(name = "测试接口文章")
public class ApArticleConfigController  {


    @Autowired
    private ApArticleConfigService apArticleConfigService;



    @GetMapping("/list")
    @Operation(method = "get" ,description = "测试文章list",summary = "文章列表")
    public AjaxResult list (){
        ApArticleConfig byId = apArticleConfigService.getById(1);
        return AjaxResult.success(byId);
    }

}

