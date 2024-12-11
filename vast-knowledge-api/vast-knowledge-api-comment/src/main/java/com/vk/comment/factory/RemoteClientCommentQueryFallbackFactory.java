package com.vk.comment.factory;


import com.vk.comment.feign.RemoteClientCommentQueryService;
import com.vk.common.core.domain.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 用户服务降级处理
 * 
 * @author vk
 */
@Component
public class RemoteClientCommentQueryFallbackFactory implements FallbackFactory<RemoteClientCommentQueryService>
{
    private static final Logger log = LoggerFactory.getLogger(RemoteClientCommentQueryFallbackFactory.class);

    @Override
    public RemoteClientCommentQueryService create(Throwable throwable)
    {
        log.error("用户服务调用失败:{}", throwable.getMessage());
        return new RemoteClientCommentQueryService()
        {
            @Override
            public R<Long> getArticleCommentById(Long id, LocalDateTime startTime, LocalDateTime endTime) {
                return R.fail("评论查询错误");
            }
        };
    }
}
