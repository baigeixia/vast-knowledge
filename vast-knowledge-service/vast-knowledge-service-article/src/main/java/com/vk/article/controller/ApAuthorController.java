package com.vk.article.controller;

import com.mybatisflex.core.paginate.Page;
import com.vk.article.domain.ApAuthor;
import com.vk.article.service.ApAuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * APP文章作者信息 控制层。
 *
 * @author 张三
 * @since 2024-07-11
 */
@RestController
@RequestMapping("/Author")
public class ApAuthorController {

    @Autowired
    private ApAuthorService apAuthorService;

    /**
     * 添加APP文章作者信息。
     *
     * @param apAuthor APP文章作者信息
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody ApAuthor apAuthor) {
        return apAuthorService.save(apAuthor);
    }

    /**
     * 根据主键删除APP文章作者信息。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Serializable id) {
        return apAuthorService.removeById(id);
    }

    /**
     * 根据主键更新APP文章作者信息。
     *
     * @param apAuthor APP文章作者信息
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody ApAuthor apAuthor) {
        return apAuthorService.updateById(apAuthor);
    }

    /**
     * 查询所有APP文章作者信息。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public List<ApAuthor> list() {
        return apAuthorService.list();
    }

    /**
     * 根据APP文章作者信息主键获取详细信息。
     *
     * @param id APP文章作者信息主键
     * @return APP文章作者信息详情
     */
    @GetMapping("getInfo/{id}")
    public ApAuthor getInfo(@PathVariable Serializable id) {
        return apAuthorService.getById(id);
    }

    /**
     * 分页查询APP文章作者信息。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<ApAuthor> page(Page<ApAuthor> page) {
        return apAuthorService.page(page);
    }

}
