package com.vk.user.controller;

import com.vk.common.core.web.domain.AjaxResult;
import com.vk.user.domain.dto.UserInfoDto;
import com.vk.user.domain.vo.LocalUserInfoVo;
import com.vk.user.domain.vo.UserInfoVo;
import com.vk.user.service.ApUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * APP用户详情信息 控制层。
 *
 * @author 张三
 * @since 2024-05-13
 */
@RestController
@RequestMapping("/info")
public class ApUserInfoController {

    @Autowired
    private ApUserInfoService apUserInfoService;

    /**
     *
     * @param type
     * @param state 1  isSendMessage   2 isRecommendMe 3 isRecommendFriend 4 isDisplayImage
     * @return
     */
    @GetMapping("/userConfig/{type}")
    public AjaxResult userConfig(
            @PathVariable(name = "type")Integer type,
            @RequestParam(name = "state")Integer state
    ){
        apUserInfoService.userConfig(state,type);
        return  AjaxResult.success();
    }

    @PostMapping("/upInfo")
    public AjaxResult upInfo(
            @RequestBody UserInfoDto dto
    ){
        apUserInfoService.upInfo(dto);
        return  AjaxResult.success();
    }

    @GetMapping("/getInfo")
    public AjaxResult getInfo(
            @RequestParam(name = "id",required = false) Long id
    ){
        UserInfoVo result=apUserInfoService.getInfo(id);
        return AjaxResult.success(result);
    }



}
