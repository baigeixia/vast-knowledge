package com.vk.article.controller;

import com.mybatisflex.core.paginate.Page;
import com.vk.article.domain.ApArticleContent;
import com.vk.article.service.ApArticleContentService;
import com.vk.common.core.web.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * APP已发布文章内容 控制层。
 *
 * @author 张三
 * @since 2024-07-11
 */
@RestController
@RequestMapping("/content")
public class ApArticleContentController {

    @Autowired
    private ApArticleContentService apArticleContentService;

    /**
     * 添加APP已发布文章内容。
     *
     * @param apArticleContent APP已发布文章内容
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public AjaxResult contentSave(@RequestBody ApArticleContent apArticleContent) {
        apArticleContentService.contentSave(apArticleContent);
        return AjaxResult.success("保存完成");
    }

    /**
     * 根据主键删除APP已发布文章内容。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Serializable id) {
        return apArticleContentService.removeById(id);
    }

    /**
     * 根据主键更新APP已发布文章内容。
     *
     * @param apArticleContent APP已发布文章内容
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody ApArticleContent apArticleContent) {
        return apArticleContentService.updateById(apArticleContent);
    }

    /**
     * 查询所有APP已发布文章内容。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public List<ApArticleContent> list() {
        return apArticleContentService.list();
    }

    /**
     * 根据APP已发布文章内容主键获取详细信息。
     *
     * @param id APP已发布文章内容主键
     * @return APP已发布文章内容详情
     */
    @GetMapping("getInfo/{id}")
    public ApArticleContent getInfo(@PathVariable Serializable id) {
        return apArticleContentService.getById(id);
    }

    /**
     * 分页查询APP已发布文章内容。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<ApArticleContent> page(Page<ApArticleContent> page) {
        return apArticleContentService.page(page);
    }

}
