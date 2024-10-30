package com.vk.user.controller;

import com.vk.common.core.web.domain.AjaxResult;
import com.vk.common.es.domain.UserInfoDocument;
import com.vk.user.domain.dto.UserInfoDto;
import com.vk.user.domain.vo.InfoRelationVo;
import com.vk.user.domain.vo.LocalUserInfoVo;
import com.vk.user.domain.vo.SearchUserInfoVo;
import com.vk.user.domain.vo.UserInfoVo;
import com.vk.user.service.ApUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    /**
     * 与用户关系查询
     * @param id
     * @return
     */
    @GetMapping("/InfoRelation")
    public AjaxResult InfoRelation(
            @RequestParam(name = "id") Long id
    ){
        InfoRelationVo result=apUserInfoService.InfoRelation(id);
        return AjaxResult.success(result);
    }

    /**
     * @param page 页数
     * @param size 长度
     * @param query  搜索内容
     * @param type   头部标题 4 用户
     * @param sort   排序    0 综合排序  1 最新  2 最热
     * @param period 时间    1 不限  2 最新一天  3 最近一周  4最近一月
     * @return
     */
    @GetMapping("/search")
    public AjaxResult searchUser(
            @RequestParam(name = "query") String query,
            @RequestParam(name = "type", defaultValue = "4", required = false) Integer type,
            @RequestParam(name = "sort", defaultValue = "0", required = false) Integer sort,
            @RequestParam(name = "period", defaultValue = "1", required = false) Integer period,
            @RequestParam(name = "page",defaultValue = "1",required = false) Long page ,
            @RequestParam(name = "size" ,defaultValue = "10",required = false) Long size
    ){
       List<SearchUserInfoVo> result= apUserInfoService.searchUser(query,type,sort,period,page,size);
        return AjaxResult.success(result);
    }




}
