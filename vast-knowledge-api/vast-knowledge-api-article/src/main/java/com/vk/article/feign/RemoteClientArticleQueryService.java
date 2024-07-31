package com.vk.article.feign;

import com.vk.article.domain.ApArticle;
import com.vk.article.factory.RemoteClientArticleQueryFallbackFactory;
import com.vk.common.core.constant.SecurityConstants;
import com.vk.common.core.constant.ServiceNameConstants;
import com.vk.common.core.domain.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(contextId = "remoteClientArticleQueryService", value = ServiceNameConstants.ARTICLE_SERVICE, fallbackFactory = RemoteClientArticleQueryFallbackFactory.class)
public interface RemoteClientArticleQueryService {

    @GetMapping("/getOne")
    R<ApArticle> getOneArticle(@RequestParam(name = "id") Long id);

}
