package com.vk.behaviour.controller;

import com.mybatisflex.core.paginate.Page;
import com.vk.behaviour.domain.ApReadBehavior;
import com.vk.behaviour.domain.vo.UserFootMarkListVo;
import com.vk.behaviour.service.ApReadBehaviorService;
import com.vk.common.core.web.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * APP阅读行为 控制层。
 *
 * @author 张三
 * @since 2024-05-13
 */
@RestController
@RequestMapping("/read")
public class ApReadBehaviorController {

    @Autowired
    private ApReadBehaviorService apReadBehaviorService;


   @GetMapping("/userFootMark")
    public AjaxResult getUserFootMark(
           @RequestParam(name = "page",defaultValue = "1",required = false) Long page ,
           @RequestParam(name = "size" ,defaultValue = "5",required = false) Long size
   ){
       List<UserFootMarkListVo> result= apReadBehaviorService.getUserFootMark(page,size);
       return AjaxResult.success(result);
   }


    @GetMapping("/article")
    public AjaxResult getArticleInfo(
            @RequestParam(name = "id") Long id
    ){
        ApReadBehavior result= apReadBehaviorService.getArticleInfo(id);
        return AjaxResult.success(result);
    }


}
