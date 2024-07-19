package com.vk.article.controller;

import com.mybatisflex.core.paginate.Page;
import com.vk.article.domain.ApArticleLabel;
import com.vk.article.service.ApArticleLabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * 文章标签信息 控制层。
 *
 * @author 张三
 * @since 2024-07-11
 */
@RestController
@RequestMapping("/Label")
public class ApArticleLabelController {

    @Autowired
    private ApArticleLabelService apArticleLabelService;

    /**
     * 添加文章标签信息。
     *
     * @param apArticleLabel 文章标签信息
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody ApArticleLabel apArticleLabel) {
        return apArticleLabelService.save(apArticleLabel);
    }

    /**
     * 根据主键删除文章标签信息。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Serializable id) {
        return apArticleLabelService.removeById(id);
    }

    /**
     * 根据主键更新文章标签信息。
     *
     * @param apArticleLabel 文章标签信息
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody ApArticleLabel apArticleLabel) {
        return apArticleLabelService.updateById(apArticleLabel);
    }

    /**
     * 查询所有文章标签信息。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public List<ApArticleLabel> list() {
        return apArticleLabelService.list();
    }

    /**
     * 根据文章标签信息主键获取详细信息。
     *
     * @param id 文章标签信息主键
     * @return 文章标签信息详情
     */
    @GetMapping("getInfo/{id}")
    public ApArticleLabel getInfo(@PathVariable Serializable id) {
        return apArticleLabelService.getById(id);
    }

    /**
     * 分页查询文章标签信息。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<ApArticleLabel> page(Page<ApArticleLabel> page) {
        return apArticleLabelService.page(page);
    }

}
