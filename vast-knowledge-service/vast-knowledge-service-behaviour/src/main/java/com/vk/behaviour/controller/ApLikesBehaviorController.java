package com.vk.behaviour.controller;

import com.mybatisflex.core.paginate.Page;
import com.vk.behaviour.domain.ApLikesBehavior;
import com.vk.behaviour.domain.dto.LikesBehaviorDto;
import com.vk.behaviour.domain.vo.LikeNotificationListVo;
import com.vk.behaviour.service.ApLikesBehaviorService;
import com.vk.common.core.web.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * APP点赞行为 控制层。
 *
 * @author 张三
 * @since 2024-05-13
 */
@RestController
@RequestMapping("/likes")
public class ApLikesBehaviorController {

    @Autowired
    private ApLikesBehaviorService apLikesBehaviorService;



    /**
     * 查询所有APP点赞行为。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public AjaxResult likeList(
            @RequestParam(name = "page",defaultValue = "1",required = false) Long page ,
            @RequestParam(name = "size" ,defaultValue = "5",required = false) Long size
    ) {
        List<LikeNotificationListVo> result= apLikesBehaviorService.likeList(page,size);
        return AjaxResult.success(result);
    }



}
