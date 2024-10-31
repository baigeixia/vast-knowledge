package com.vk.behaviour.controller;

import com.mybatisflex.core.paginate.Page;
import com.vk.behaviour.domain.ApFollowBehavior;
import com.vk.behaviour.domain.vo.notification.follow.FollowNotificationListVo;
import com.vk.behaviour.service.ApFollowBehaviorService;
import com.vk.common.core.web.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * APP关注行为 控制层。
 *
 * @author 张三
 * @since 2024-05-13
 */
@RestController
@RequestMapping("/follow")
public class ApFollowBehaviorController {

    @Autowired
    private ApFollowBehaviorService apFollowBehaviorService;


    /**
     * 查询所有APP关注行为。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public AjaxResult followList(
            @RequestParam(name = "id",required = false) Long id ,
            @RequestParam(name = "page",defaultValue = "1",required = false) Long page ,
            @RequestParam(name = "size" ,defaultValue = "5",required = false) Long size
    ) {
       List<FollowNotificationListVo> result= apFollowBehaviorService.followList(id,page,size);
        return AjaxResult.success(result);
    }

}
