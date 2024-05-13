package com.vk.admin.controller;

import com.mybatisflex.core.paginate.Page;
import com.vk.admin.domain.ApUserFeedback;
import com.vk.admin.service.ApUserFeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * APP用户反馈信息 控制层。
 *
 * @author 张三
 * @since 2024-05-13
 */
@RestController
@RequestMapping("/apUserFeedback")
public class ApUserFeedbackController {

    @Autowired
    private ApUserFeedbackService apUserFeedbackService;

    /**
     * 添加APP用户反馈信息。
     *
     * @param apUserFeedback APP用户反馈信息
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody ApUserFeedback apUserFeedback) {
        return apUserFeedbackService.save(apUserFeedback);
    }

    /**
     * 根据主键删除APP用户反馈信息。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Serializable id) {
        return apUserFeedbackService.removeById(id);
    }

    /**
     * 根据主键更新APP用户反馈信息。
     *
     * @param apUserFeedback APP用户反馈信息
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody ApUserFeedback apUserFeedback) {
        return apUserFeedbackService.updateById(apUserFeedback);
    }

    /**
     * 查询所有APP用户反馈信息。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public List<ApUserFeedback> list() {
        return apUserFeedbackService.list();
    }

    /**
     * 根据APP用户反馈信息主键获取详细信息。
     *
     * @param id APP用户反馈信息主键
     * @return APP用户反馈信息详情
     */
    @GetMapping("getInfo/{id}")
    public ApUserFeedback getInfo(@PathVariable Serializable id) {
        return apUserFeedbackService.getById(id);
    }

    /**
     * 分页查询APP用户反馈信息。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<ApUserFeedback> page(Page<ApUserFeedback> page) {
        return apUserFeedbackService.page(page);
    }

}
