package com.vk.search.controller;

import com.mybatisflex.core.paginate.Page;
import com.vk.common.core.web.domain.AjaxResult;
import com.vk.search.domain.ApHotWords;
import com.vk.search.service.ApHotWordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * 搜索热词 控制层。
 *
 * @author 张三
 * @since 2024-05-13
 */
@RestController
@RequestMapping("/hot")
public class ApHotWordsController {

    @Autowired
    private ApHotWordsService apHotWordsService;


    @GetMapping("/trending")
    public AjaxResult getTrending(
    ) {
        List<ApHotWords>  result=apHotWordsService.getTrending();
        return AjaxResult.success(result);
    }

}
