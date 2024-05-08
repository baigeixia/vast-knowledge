package com.vk.article.controller;



import com.vk.article.domain.ApArticleConfig;
import com.vk.article.service.ApArticleConfigService;
import com.vk.common.core.web.domain.AjaxResult;
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
public class ApArticleConfigController  {


    @Autowired
    private ApArticleConfigService apArticleConfigService;



    @GetMapping("/list")
    public AjaxResult list (){
        ApArticleConfig byId = apArticleConfigService.getById(1);
        return AjaxResult.success(byId);
    }

}

