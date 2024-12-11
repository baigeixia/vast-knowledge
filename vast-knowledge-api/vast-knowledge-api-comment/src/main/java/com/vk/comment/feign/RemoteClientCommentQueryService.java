package com.vk.comment.feign;

import com.vk.comment.factory.RemoteClientCommentQueryFallbackFactory;
import com.vk.common.core.constant.ServiceNameConstants;
import com.vk.common.core.domain.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@FeignClient(contextId = "remoteClientCommentQueryService", value = ServiceNameConstants.ANALYZE_COMMENT, fallbackFactory = RemoteClientCommentQueryFallbackFactory.class)
public interface RemoteClientCommentQueryService {

    @GetMapping("/Comment/getCommentTotalById")
    R<Long> getArticleCommentById(
            @RequestParam(name = "id") Long id,
            @RequestParam(name = "startTime") LocalDateTime startTime,
            @RequestParam(name = "endTime") LocalDateTime endTime
    );
}
