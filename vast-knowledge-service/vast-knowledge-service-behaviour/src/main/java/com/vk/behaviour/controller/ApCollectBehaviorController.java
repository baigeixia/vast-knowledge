package com.vk.behaviour.controller;

import com.mybatisflex.core.paginate.Page;
import com.vk.article.domain.HomeArticleListVo;
import com.vk.behaviour.domain.ApCollectBehavior;
import com.vk.behaviour.service.ApCollectBehaviorService;
import com.vk.common.core.web.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * APP收藏行为 控制层。
 *
 * @author 张三
 * @since 2024-09-03
 */
@RestController
@RequestMapping("/collect")
public class ApCollectBehaviorController {

    @Autowired
    private ApCollectBehaviorService apCollectBehaviorService;


    /**
     * 查询APP收藏行为。
     */
    @GetMapping("list")
    public AjaxResult userCollectList(
            @RequestParam(name = "page",defaultValue = "1",required = false) Long page ,
            @RequestParam(name = "size" ,defaultValue = "5",required = false) Long size ,
            @RequestParam(name = "userId" ,required = false) Long userId
    ) {
        List<HomeArticleListVo> resultInfo=apCollectBehaviorService.userCollectList(page,size,userId);
        return AjaxResult.success(resultInfo) ;
    }

}
