package com.vk.comment.controller;

import com.mybatisflex.core.paginate.Page;
import com.vk.comment.domain.ApCommentRepay;
import com.vk.comment.domain.dto.CommentReSaveDto;
import com.vk.comment.domain.dto.CommentSaveDto;
import com.vk.comment.service.ApCommentRepayService;
import com.vk.common.core.web.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * APP评论回复信息 控制层。
 *
 * @author 张三
 * @since 2024-05-13
 */
@RestController
@RequestMapping("/CommentRepay")
public class ApCommentRepayController {

    @Autowired
    private ApCommentRepayService apCommentRepayService;


    @PostMapping("saveCommentRe")
    public AjaxResult saveCommentRe(@RequestBody CommentReSaveDto dto) {
        apCommentRepayService.saveCommentRe(dto);
        return AjaxResult.success();
    }



    /**
     * 根据主键删除APP评论回复信息。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public AjaxResult removeCommentRe(@PathVariable Serializable id) {
        apCommentRepayService.removeCommentRe(id);
        return AjaxResult.success();
    }

    /**
     * 根据顶级父级 主键查询回复 评论信息。
     *
     * @param commentId 顶级父级 主键
     * @return APP评论信息
     */
    @GetMapping("getCommentReList")
    public AjaxResult getCommentReList(
            @RequestParam Serializable commentId,
            @RequestParam(required = false,defaultValue = "1") Long page,
            @RequestParam(required = false,defaultValue = "10") Long size
    ) {

        Page<ApCommentRepay> result = apCommentRepayService.getCommentReList(commentId,page,size);

        return AjaxResult.success(result);
    }

}
