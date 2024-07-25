package com.vk.article.controller;

import com.mybatisflex.core.paginate.Page;
import com.vk.article.domain.dto.ArticleAndConfigDto;
import com.vk.article.domain.vo.ArticleInfoVo;
import com.vk.article.domain.dto.HomeArticleListDto;
import com.vk.article.domain.vo.ArticleListVo;
import com.vk.article.service.ApArticleService;
import com.vk.common.core.web.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * 已发布的文章信息 控制层。
 *
 * @author 张三
 * @since 2024-07-11
 */
@RestController
@RequestMapping("/article")
public class ApArticleController {

    @Autowired
    private ApArticleService apArticleService;


    @PostMapping("save")
    public AjaxResult saveArticle(@RequestBody ArticleAndConfigDto  dto) {
         Long id= apArticleService.saveOrUpArticle(dto);
        return AjaxResult.success("保存完成",id) ;
    }

    @GetMapping("info")
    public AjaxResult infoArticle(@RequestParam(name = "id" ) Long id) {
        ArticleInfoVo resultInfo=apArticleService.infoArticle(id);
        return AjaxResult.success(resultInfo) ;
    }

    /**
     * 查询 首页文章列表
     * @param page
     * @param size
     * @param tag
     * @return
     */
    @GetMapping("homeList")
    public AjaxResult listArticle(
            @RequestParam(name = "page",defaultValue = "1",required = false) Long page ,
            @RequestParam(name = "size" ,defaultValue = "5",required = false) Long size ,
            @RequestParam(name = "tag",defaultValue = "1",required = false) Integer tag
    ) {
        Page<HomeArticleListDto> resultInfo=apArticleService.listArticle(page,size,tag);
        return AjaxResult.success(resultInfo) ;
    }


    /**
     * 查询 当前用户文章列表
     * @param page
     * @param size
     * @return
     */
    @GetMapping("articleList")
    public AjaxResult articleListArticle(
            @RequestParam(name = "page",defaultValue = "1",required = false) Long page ,
            @RequestParam(name = "size" ,defaultValue = "10",required = false) Long size ,
            @RequestParam(name = "status" ,defaultValue = "1",required = false) Integer status,
            @RequestParam(name = "title") String title,
            @RequestParam(name = "channelId") Long channelId,
            @RequestParam(name = "startTime") LocalDateTime startTime,
            @RequestParam(name = "endTime") LocalDateTime endTime
    ) {
        Page<ArticleListVo> resultInfo=apArticleService.articleListArticle(page,size,status,title,channelId,startTime,endTime);
        return AjaxResult.success(resultInfo) ;
    }


}
