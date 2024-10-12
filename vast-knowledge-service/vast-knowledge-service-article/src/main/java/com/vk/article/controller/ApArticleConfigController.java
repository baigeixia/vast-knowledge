package com.vk.article.controller;

import com.mybatisflex.core.paginate.Page;
import com.vk.article.domain.ApArticleConfig;
import com.vk.article.service.ApArticleConfigService;
import com.vk.common.core.web.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * APP已发布文章配置 控制层。
 *
 * @author 张三
 * @since 2024-07-11
 */
@RestController
@RequestMapping("/config")
public class ApArticleConfigController {

    @Autowired
    private ApArticleConfigService apArticleConfigService;

    /**
     * 文章配置
     * @param id
     * @return
     */
    @GetMapping("/getArticle")
    public AjaxResult getArticle(
            @RequestParam("id") Long id
    ){
        return AjaxResult.success("");
    }

}
