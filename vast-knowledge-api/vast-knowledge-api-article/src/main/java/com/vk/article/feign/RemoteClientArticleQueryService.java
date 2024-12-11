package com.vk.article.feign;

import com.vk.article.domain.ApArticle;
import com.vk.article.domain.HomeArticleListVo;
import com.vk.article.factory.RemoteClientArticleQueryFallbackFactory;
import com.vk.common.core.constant.ServiceNameConstants;
import com.vk.common.core.domain.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

@FeignClient(contextId = "remoteClientArticleQueryService", value = ServiceNameConstants.ARTICLE_SERVICE, fallbackFactory = RemoteClientArticleQueryFallbackFactory.class)
public interface RemoteClientArticleQueryService {

    @GetMapping("/article/getOne")
    R<ApArticle> getOneArticle(@RequestParam(name = "id") Long id);

    @PostMapping("article/getTitle")
    public R<Map<Long,String>> getArticleTitle(
            @RequestBody Set<Long> ids
    );


    @GetMapping("article/getArticleIdList")
     R<Map<Long, HomeArticleListVo>> getArticleIdList(
            @RequestBody Set<Long> ids
    );

    @GetMapping("article/getBehaviorArticleIdList")
    R<Map<Long, HomeArticleListVo>> getBehaviorArticleIdList(
            @RequestParam(name = "userId") Long userId,
            @RequestParam(name = "page") Long page,
            @RequestParam(name = "ids") Set<Long> ids
    );


    @GetMapping("article/getSearchArticleList")
    R<List< HomeArticleListVo>> getSearchArticleList(
            @RequestParam(name = "query") String query,
            @RequestParam(name = "type", defaultValue = "0", required = false) Integer type,
            @RequestParam(name = "sort", defaultValue = "0", required = false) Integer sort,
            @RequestParam(name = "period", defaultValue = "1", required = false) Integer period,
            @RequestParam(name = "page",defaultValue = "1",required = false) Long page ,
            @RequestParam(name = "size" ,defaultValue = "10",required = false) Long size
    );



}
