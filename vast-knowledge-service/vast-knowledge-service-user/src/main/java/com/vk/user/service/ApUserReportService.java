package com.vk.user.service;

import com.mybatisflex.core.service.IService;
import com.vk.user.domain.ApUserReport;
import com.vk.user.domain.dto.ReportDto;


/**
 * APP用户举报信息 服务层。
 *
 * @author 张三
 * @since 2024-09-09
 */
public interface ApUserReportService extends IService<ApUserReport> {

    void reportSave(ReportDto dto);
}
