package com.vk.user.controller;

import com.mybatisflex.core.paginate.Page;
import com.vk.common.core.web.domain.AjaxResult;
import com.vk.user.domain.ApUserLetter;
import com.vk.user.domain.vo.MsgListVo;
import com.vk.user.domain.vo.MsgUserListVo;
import com.vk.user.service.ApUserLetterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * APP用户私信信息 控制层。
 *
 * @author 张三
 * @since 2024-05-13
 */
@RestController
@RequestMapping("/letter")
public class ApUserLetterController {

    @Autowired
    private ApUserLetterService apUserLetterService;


    /**
     * 查询所有APP用户。
     *type 1最近联系人   2默认人  3互相关注
     * @return 所有数据
     */
    @GetMapping("list")
    public AjaxResult letterList(
            @RequestParam(name = "type",required = false,defaultValue = "1") Integer type,
            @RequestParam(name = "page",required = false,defaultValue = "1") Integer page,
            @RequestParam(name = "size",required = false,defaultValue = "10") Integer size
    ) {
    List<MsgUserListVo> result=    apUserLetterService.letterList(type,page,size);
        return AjaxResult.success(result);
    }


    /**
     * 查询所有APP用户私信信息。
     * @return 所有数据
     */
    @GetMapping("msgList")
    public AjaxResult msgList(
            @RequestParam(name = "userId") Long userId,
            @RequestParam(name = "page",required = false,defaultValue = "1") Integer page,
            @RequestParam(name = "size",required = false,defaultValue = "10") Integer size
    ) {
        MsgListVo result= apUserLetterService.msgList(userId,page,size);
        return AjaxResult.success(result);
    }


    @GetMapping("clearUnreadMsg")
    public AjaxResult clearUnreadMsg(
            @RequestParam(name = "userId") Long userId
    ) {
         apUserLetterService.clearUnreadMsg(userId);
        return AjaxResult.success();
    }


    @DeleteMapping("delMsg")
    public AjaxResult delMsg(
            @RequestParam(name = "msgId") Long msgId
    ) {
        apUserLetterService.delMsg(msgId);
        return AjaxResult.success();
    }



}
