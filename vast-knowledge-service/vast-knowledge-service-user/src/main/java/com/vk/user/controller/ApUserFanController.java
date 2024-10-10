package com.vk.user.controller;

import com.mybatisflex.core.paginate.Page;
import com.vk.common.core.web.domain.AjaxResult;
import com.vk.user.domain.vo.FanListVo;
import com.vk.user.service.ApUserFanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * APP用户粉丝信息 控制层。
 *
 * @author 张三
 * @since 2024-05-13
 */
@RestController
@RequestMapping("/fan")
public class ApUserFanController {

    @Autowired
    private ApUserFanService apUserFanService;
    /**
     * 查询 用户粉丝
     * @param page
     * @param size
     * @return
     */
    @GetMapping("list")
    public AjaxResult getList(
            @RequestParam(name = "page",defaultValue = "1",required = false) Long page ,
            @RequestParam(name = "size" ,defaultValue = "5",required = false) Long size ,
            @RequestParam(name = "userId" ,required = false) Long userId
    ) {
        Page<FanListVo> resultInfo=apUserFanService.getList(page,size,userId);
        return AjaxResult.success(resultInfo) ;
    }


}
