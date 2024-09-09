package com.vk.user.controller;

import com.mybatisflex.core.paginate.Page;
import com.vk.common.core.web.domain.AjaxResult;
import com.vk.user.domain.ApUserMessage;
import com.vk.user.domain.vo.SysteamNotificationListVo;
import com.vk.user.service.ApUserMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * APP用户消息通知信息 控制层。
 *
 * @author 张三
 * @since 2024-05-13
 */
@RestController
@RequestMapping("/message")
public class ApUserMessageController {

    @Autowired
    private ApUserMessageService apUserMessageService;



    /**
     * 查询所有APP用户消息通知信息。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public AjaxResult getSystemList(
            @RequestParam(name = "page",defaultValue = "1",required = false) Long page ,
            @RequestParam(name = "size" ,defaultValue = "5",required = false) Long size
    ) {
      List<SysteamNotificationListVo> result=  apUserMessageService.getSystemList(page,size);
        return  AjaxResult.success(result);
    }



}
