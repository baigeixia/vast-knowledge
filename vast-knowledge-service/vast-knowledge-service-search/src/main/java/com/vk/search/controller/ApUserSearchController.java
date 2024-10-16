package com.vk.search.controller;

import com.mybatisflex.core.paginate.Page;
import com.vk.search.domain.ApUserSearch;
import com.vk.search.service.ApUserSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * APP用户搜索信息 控制层。
 *
 * @author 张三
 * @since 2024-05-13
 */
@RestController
@RequestMapping("/search")
public class ApUserSearchController {

    @Autowired
    private ApUserSearchService apUserSearchService;



}
