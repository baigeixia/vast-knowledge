package com.vk.user.controller;

import com.mybatisflex.core.paginate.Page;

import com.vk.common.core.web.domain.AjaxResult;
import com.vk.user.domain.ApUserReport;
import com.vk.user.domain.dto.ReportDto;
import com.vk.user.service.ApUserReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * APP用户举报信息 控制层。
 *
 * @author 张三
 * @since 2024-09-09
 */
@RestController
@RequestMapping("/apUserReport")
public class ApUserReportController {

    @Autowired
    private ApUserReportService apUserReportService;

    /**
     * 添加APP用户举报信息。
     */
    @PostMapping("save")
    public AjaxResult reportSave(@RequestBody ReportDto dto) {
        apUserReportService.reportSave(dto);
        return AjaxResult.success();
    }

    /**
     * 根据主键删除APP用户举报信息。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Serializable id) {
        return apUserReportService.removeById(id);
    }

    /**
     * 根据主键更新APP用户举报信息。
     *
     * @param apUserReport APP用户举报信息
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody ApUserReport apUserReport) {
        return apUserReportService.updateById(apUserReport);
    }

    /**
     * 查询所有APP用户举报信息。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public List<ApUserReport> list() {
        return apUserReportService.list();
    }

    /**
     * 根据APP用户举报信息主键获取详细信息。
     *
     * @param id APP用户举报信息主键
     * @return APP用户举报信息详情
     */
    @GetMapping("getInfo/{id}")
    public ApUserReport getInfo(@PathVariable Serializable id) {
        return apUserReportService.getById(id);
    }

    /**
     * 分页查询APP用户举报信息。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<ApUserReport> page(Page<ApUserReport> page) {
        return apUserReportService.page(page);
    }

}
