package com.vk.comment.controller;

import com.mybatisflex.core.paginate.Page;
import com.vk.comment.domain.ApComment;
import com.vk.comment.service.ApCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

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
     * @param apComment APP评论信息
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody ApComment apComment) {
        return apCommentService.save(apComment);
    }

    /**
     * 根据主键删除APP评论信息。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Serializable id) {
        return apCommentService.removeById(id);
    }

    /**
     * 根据主键更新APP评论信息。
     *
     * @param apComment APP评论信息
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody ApComment apComment) {
        return apCommentService.updateById(apComment);
    }

    /**
     * 查询所有APP评论信息。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public List<ApComment> list() {
        return apCommentService.list();
    }

    /**
     * 根据APP评论信息主键获取详细信息。
     *
     * @param id APP评论信息主键
     * @return APP评论信息详情
     */
    @GetMapping("getInfo/{id}")
    public ApComment getInfo(@PathVariable Serializable id) {
        return apCommentService.getById(id);
    }

    /**
     * 分页查询APP评论信息。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<ApComment> page(Page<ApComment> page) {
        return apCommentService.page(page);
    }

}
