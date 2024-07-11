package com.vk.article.controller;

import com.mybatisflex.core.paginate.Page;
import com.vk.article.domain.ApArticle;
import com.vk.article.service.ApArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * 已发布的文章信息 控制层。
 *
 * @author 张三
 * @since 2024-07-11
 */
@RestController
@RequestMapping("/apArticle")
public class ApArticleController {

    @Autowired
    private ApArticleService apArticleService;

    /**
     * 添加已发布的文章信息。
     *
     * @param apArticle 已发布的文章信息
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody ApArticle apArticle) {
        return apArticleService.save(apArticle);
    }

    /**
     * 根据主键删除已发布的文章信息。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Serializable id) {
        return apArticleService.removeById(id);
    }

    /**
     * 根据主键更新已发布的文章信息。
     *
     * @param apArticle 已发布的文章信息
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody ApArticle apArticle) {
        return apArticleService.updateById(apArticle);
    }

    /**
     * 查询所有已发布的文章信息。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public List<ApArticle> list() {
        return apArticleService.list();
    }

    /**
     * 根据已发布的文章信息主键获取详细信息。
     *
     * @param id 已发布的文章信息主键
     * @return 已发布的文章信息详情
     */
    @GetMapping("getInfo/{id}")
    public ApArticle getInfo(@PathVariable Serializable id) {
        return apArticleService.getById(id);
    }

    /**
     * 分页查询已发布的文章信息。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<ApArticle> page(Page<ApArticle> page) {
        return apArticleService.page(page);
    }

}
