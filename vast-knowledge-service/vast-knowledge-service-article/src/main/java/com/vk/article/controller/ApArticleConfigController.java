package com.vk.article.controller;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.vk.article.domain.ApArticleConfig;
import com.vk.article.domain.table.ApArticleConfigTableDef;
import com.vk.article.domain.table.ApArticleTableDef;
import com.vk.article.service.ApArticleConfigService;
import com.vk.common.core.exception.LeadNewsException;
import com.vk.common.core.web.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import reactor.bool.BooleanUtils;

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


    @DeleteMapping("/deleteArticle")
    public AjaxResult deleteArticle(
            @RequestParam("id") Long id
    ){
        apArticleConfigService.deleteArticle(id);
        return AjaxResult.success();
    }

    /**
     *
     * @param id 文章id
     * @param downIs 0上架 1下架
     * @param deleteIs  0 不删除 1删除
     * @return
     */
    @GetMapping("/down")
    public AjaxResult downArticle(
            @RequestParam("id") Long id,
            @RequestParam(value = "downIs",required = false) Boolean downIs,
            @RequestParam(value = "deleteIs",required = false) Boolean deleteIs
    ){
        if (downIs==null && deleteIs ==null){
            throw new LeadNewsException("参数错误");
        }

        ApArticleConfig byId = apArticleConfigService.getOne(QueryWrapper.create().where(ApArticleConfigTableDef.AP_ARTICLE_CONFIG.ARTICLE_ID.eq(id)));

        if (ObjectUtils.isEmpty(byId)){
            throw new LeadNewsException("文章不存在");
        }

        ApArticleConfig config = new ApArticleConfig();
        config.setId(byId.getId());

        Boolean isDown = byId.getIsDown();
        if (downIs != null){
            if (downIs.equals(isDown))   throw new LeadNewsException("已经是 " +(isDown ? "下架":"上架") +" 状态");
            config.setIsDown(downIs);
        }

        Boolean isDelete = byId.getIsDelete();
        if (deleteIs != null){
            if (deleteIs.equals(isDelete))   throw new LeadNewsException("已经是 " +(isDelete ? "删除":"取消删除") );
            config.setIsDelete(deleteIs);
        }

        apArticleConfigService.updateById(config);

        return AjaxResult.success();
    }


}
