package com.vk.behaviour.controller;

import com.vk.behaviour.domain.ApBehaviorEntry;
import com.vk.behaviour.domain.vo.BehaviorListVo;
import com.vk.behaviour.service.ApBehaviorEntryService;
import com.vk.common.core.web.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * APP行为实体,一个行为实体可能是用户或者设备，或者其它 控制层。
 *
 * @author 张三
 * @since 2024-05-13
 */
@RestController
@RequestMapping("/behavior")
public class ApBehaviorEntryController {

    @Autowired
    private ApBehaviorEntryService apBehaviorEntryService;

    /**
     * 查询所有APP行为实体,一个行为实体可能是用户或者设备，或者其它。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public AjaxResult getList(
            @RequestParam(name = "userId",required = false) Long userId ,
            @RequestParam(name = "page",defaultValue = "1",required = false) Long page ,
            @RequestParam(name = "size" ,defaultValue = "5",required = false) Long size
    ) {
      List<BehaviorListVo> vo= apBehaviorEntryService.getList(userId,page,size);
        return AjaxResult.success(vo);
    }

}
