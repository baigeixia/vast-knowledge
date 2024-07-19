package com.vk.article.controller;

import com.mybatisflex.core.paginate.Page;
import com.vk.article.domain.ApArticle;
import com.vk.article.domain.ApArticleContent;
import com.vk.article.domain.vo.ArticleInfoVo;
import com.vk.article.service.ApArticleService;
import com.vk.common.core.web.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * 已发布的文章信息 控制层。
 *
 * @author 张三
 * @since 2024-07-11
 */
@RestController
@RequestMapping("/Article")
public class ApArticleController {

    @Autowired
    private ApArticleService apArticleService;


    @PostMapping("save")
    public AjaxResult saveArticle(@RequestBody ApArticle apArticle) {
         Long id= apArticleService.saveArticle(apArticle);
        return AjaxResult.success("添加成功",id) ;
    }

    @GetMapping("info")
    public AjaxResult infoArticle(@RequestParam Long articleId) {
        ArticleInfoVo resultInfo=apArticleService.infoArticle(articleId);
        return AjaxResult.success(resultInfo) ;
    }

    /**
     * 根据主键更新APP已发布文章内容。
     *
     * @param apArticle APP已发布文章
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody ApArticle apArticle) {
        return apArticleService.updateById(apArticle);
    }


}
