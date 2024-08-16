package com.vk.comment.controller;

import com.vk.comment.document.ApCommentDocument;
import com.vk.comment.domain.dto.CommentSaveDto;
import com.vk.comment.domain.dto.UpCommentDto;
import com.vk.comment.domain.vo.CommentList;
import com.vk.comment.domain.vo.CommentListVo;
import com.vk.comment.service.ApCommentService;
import com.vk.common.core.web.domain.AjaxResult;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;

/**
 * APP评论信息 控制层。
 *
 * @author 张三
 * @since 2024-05-13
 */
@RestController
@RequestMapping("/Comment")
public class ApCommentController {

    @Autowired
    private ApCommentService apCommentService;

    /**
     * 添加APP评论信息。
     *
     * @param dto APP评论信息
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("saveComment")
    public AjaxResult saveComment(@RequestBody CommentSaveDto dto) {
        CommentList result = apCommentService.saveComment(dto);
        return AjaxResult.success(result);
    }


    /**
     * 根据主键删除APP评论信息。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public AjaxResult removeComment(@PathVariable Serializable id) {
        apCommentService.removeComment(id);
        return AjaxResult.success();
    }

    /**
     * 根据主键更新APP评论信息。
     *
     * @param dto APP评论信息
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public AjaxResult updateComment(@RequestBody UpCommentDto dto) {
        apCommentService.updateComment(dto);
        return AjaxResult.success();
    }


    /**
     * 根据文章主键查询APP评论信息。
     *
     * @param entryId 文章主键
     * @return APP评论信息
     */
    @GetMapping("getCommentList")
    public AjaxResult getCommentList(
            @RequestParam(name = "entryId") Long entryId,
            @RequestParam(name = "type",required = false,defaultValue = "0") Integer type,
            @RequestParam(name = "page",required = false,defaultValue = "1") Long page,
            @RequestParam(name = "size",required = false,defaultValue = "10") Long size
    ) {
        System.out.println("---> " + Thread.currentThread());
        CommentListVo result = apCommentService.getCommentList(entryId,type,page,size);
        service.test();
        return AjaxResult.success(result);
    }
    @Resource
    private TestService service;




    @GetMapping("/test")
    public String test(){
        service.test();
        return "hello";
    }

    @GetMapping("/test2/{id}")
    public ApCommentDocument test2(@PathVariable(name = "id") Long id){
        return service.test2(id);
    }
}
