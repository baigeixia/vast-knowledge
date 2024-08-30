package com.vk.behaviour.controller;

import com.mybatisflex.core.paginate.Page;
import com.vk.behaviour.domain.ApLikesBehavior;
import com.vk.behaviour.domain.dto.LikesBehaviorDto;
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
@RequestMapping("/LikesBehavior")
public class ApLikesBehaviorController {

    @Autowired
    private ApLikesBehaviorService apLikesBehaviorService;

    /**
     * 添加APP点赞行为。
     *
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public AjaxResult saveLike(@RequestBody LikesBehaviorDto dto) {
         apLikesBehaviorService.saveLike(dto);
        return AjaxResult.success("保存成功");
    }


    /**
     * 查询所有APP点赞行为。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public List<ApLikesBehavior> list() {
        return apLikesBehaviorService.list();
    }



}
