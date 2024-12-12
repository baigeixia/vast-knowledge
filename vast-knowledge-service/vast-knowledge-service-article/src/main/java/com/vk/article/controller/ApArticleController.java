package com.vk.article.controller;

import com.mybatisflex.core.paginate.Page;
import com.vk.article.domain.ApArticle;
import com.vk.article.domain.HomeArticleListVo;
import com.vk.article.domain.dto.ArticleAndConfigDto;
import com.vk.article.domain.vo.ArticleDataVo;
import com.vk.article.domain.vo.ArticleInfoVo;
import com.vk.article.domain.vo.ArticleListVo;
import com.vk.article.service.ApArticleService;
import com.vk.common.core.domain.R;
import com.vk.common.core.web.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    @GetMapping("authorInfo")
    public AjaxResult authorInfo(@RequestParam(name = "id" ) Long id) {
        ArticleInfoVo resultInfo=apArticleService.authorInfo(id);
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
            @RequestParam(name = "tag",defaultValue = "1",required = false) Integer tag,
            @RequestParam(name = "type",defaultValue = "0",required = false) Integer type
    ) {
        Page<HomeArticleListVo> resultInfo=apArticleService.listArticle(page,size,tag,type);
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
            @RequestParam(name = "title" ,required = false) String title,
            @RequestParam(name = "channelId" ,required = false) Long channelId,
            @RequestParam(name = "startTime" ,required = false) String startTime,
            @RequestParam(name = "endTime" ,required = false) String endTime
    ) {
        Page<ArticleListVo> resultInfo=apArticleService.articleListArticle(page,size,status,title,channelId,startTime,endTime);
        return AjaxResult.success(resultInfo) ;
    }


    /**
     * 查询 当前用户文章列表
     * @param page
     * @param size
     * @return
     */
    @GetMapping("posts")
    public AjaxResult userArticleList(
            @RequestParam(name = "page",defaultValue = "1",required = false) Long page ,
            @RequestParam(name = "size" ,defaultValue = "5",required = false) Long size ,
            @RequestParam(name = "type" ,defaultValue = "1",required = false) Integer type ,
            @RequestParam(name = "userId" ,required = false) Long userId
    ) {
        Page<HomeArticleListVo> resultInfo=apArticleService.userArticleList(page,size,type,userId);
        return AjaxResult.success(resultInfo) ;
    }

    /**
     * 是否存在  true 存在 false  不存在
     * @param id
     * @return
     */

    @GetMapping("getOne")
    public R<ApArticle> getOneArticle(
            @RequestParam(name = "id") Long id
    ) {
        ApArticle byId = apArticleService.getById(id);
        return R.ok(byId )  ;
    }

    /**
     * 通过 set Id  获取文章 标题
     * @param ids 文章id
     * @return
     */

    @PostMapping("getTitle")
    public R<Map<Long,String>> getArticleTitle(
            @RequestBody Set<Long> ids
    ) {
        Map<Long,String> byId = apArticleService.getArticleTitle(ids);
        return R.ok(byId )  ;
    }

    @PostMapping("getArticleIdList")
    public R<Map<Long, HomeArticleListVo>> getArticleIdList(
            @RequestBody Set<Long> ids
    ) {
        Map<Long, HomeArticleListVo> articleIdList = apArticleService.getArticleIdList(ids);
        return R.ok(articleIdList)  ;
    }

    @GetMapping("getBehaviorArticleIdList")
    R<Map<Long, HomeArticleListVo>> getBehaviorArticleIdList(
            @RequestParam(name = "userId") Long userId,
            @RequestParam(name = "page") Long page,
            @RequestParam(name = "ids") Set<Long> ids
    ){
        Map<Long, HomeArticleListVo> articleIdList = apArticleService.getBehaviorArticleIdList(userId,ids,page);
        return R.ok(articleIdList)  ;
    }

    @GetMapping("article/getSearchArticleList")
    R<List< HomeArticleListVo>> getSearchArticleList(
            @RequestParam(name = "query") String query,
            @RequestParam(name = "type", defaultValue = "0", required = false) Integer type,
            @RequestParam(name = "sort", defaultValue = "0", required = false) Integer sort,
            @RequestParam(name = "period", defaultValue = "1", required = false) Integer period,
            @RequestParam(name = "page",defaultValue = "1",required = false) Long page ,
            @RequestParam(name = "size" ,defaultValue = "10",required = false) Long size
    ){

        List< HomeArticleListVo> articleIdList = apArticleService.getSearchArticleList(query,type,sort,period,page,size);

        return R.ok(articleIdList)  ;
    }

    @DeleteMapping("/deleteOne")
    public AjaxResult deleteOne(
            @RequestParam(name = "id") Long id
    ){
        apArticleService.deleteOne(id);
        return AjaxResult.success();
    }

    @GetMapping("/getArticleData")
    public AjaxResult getArticleData(
            @RequestParam(name = "startTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @RequestParam(name = "endTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime,
            @RequestParam(name = "cycle", required = false, defaultValue = "0") Integer cycle,
            @RequestParam(name = "page",defaultValue = "1") Long page,
            @RequestParam(name = "size",defaultValue = "10") Long size
    ){
        ArticleDataVo result= apArticleService.getArticleData(startTime,endTime,cycle,page,size);
        return AjaxResult.success(result);
    }


}
