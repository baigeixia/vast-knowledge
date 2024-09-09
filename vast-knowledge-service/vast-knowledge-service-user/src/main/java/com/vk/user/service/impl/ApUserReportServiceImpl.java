package com.vk.user.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;

import com.vk.common.core.exception.CustomSimpleThrowUtils;
import com.vk.common.core.exception.LeadNewsException;
import com.vk.common.core.utils.RequestContextUtil;
import com.vk.common.core.utils.StringUtils;
import com.vk.common.core.utils.bean.BeanUtils;
import com.vk.user.domain.ApUserReport;
import com.vk.user.domain.dto.ReportDto;
import com.vk.user.mapper.ApUserReportMapper;
import com.vk.user.service.ApUserReportService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * APP用户举报信息 服务层实现。
 *
 * @author 张三
 * @since 2024-09-09
 */
@Service
public class ApUserReportServiceImpl extends ServiceImpl<ApUserReportMapper, ApUserReport> implements ApUserReportService {

    @Override
    public void reportSave(ReportDto dto) {
        Long userId = RequestContextUtil.getUserId();
        String userName = RequestContextUtil.getUserName();
        Long reportUserId = dto.getReportUserId();
        String reportUserName = dto.getReportUserName();

        if (StringUtils.isLongEmpty(reportUserId) || StringUtils.isEmpty(reportUserName)){
            throw new LeadNewsException("被举报人id或者名称不能为空");
        }

        // if (userId.equals(reportUserId)){
        //     throw new LeadNewsException("自己的文章不能举报");
        // }

        // Long articleId = dto.getArticleId();
        // CustomSimpleThrowUtils.LongIsEmpty(articleId,"文章id不能为空");

        String reason = dto.getReportReason();
        CustomSimpleThrowUtils.StringIsEmpty(reason,"举报原因不能为空");

        ApUserReport report = new ApUserReport();
        BeanUtils.copyProperties(dto,report);
        report.setUserId(userId);
        report.setUserName(userName);
        report.setIsSolve(0);
        report.setCreatedTime(LocalDateTime.now());

        mapper.insert(report);


    }
}
