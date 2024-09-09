package com.vk.user.controller;

import com.mybatisflex.core.paginate.Page;
import com.vk.common.core.web.domain.AjaxResult;
import com.vk.user.domain.ApUserFeedback;
import com.vk.user.domain.dto.FeedbackDto;
import com.vk.user.service.ApUserFeedbackService;
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
@RequestMapping("/feedback")
public class ApUserFeedbackController {

    @Autowired
    private ApUserFeedbackService apUserFeedbackService;

    /**
     * 添加APP用户反馈信息。
     *
     */
    @PostMapping("save")
    public AjaxResult feedbackSave(@RequestBody FeedbackDto dto) {
        apUserFeedbackService.feedbackSave(dto);
        return AjaxResult.success();
    }


}
