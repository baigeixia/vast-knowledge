package com.vk.article.factory;


import com.vk.article.domain.ApArticle;
import com.vk.article.feign.RemoteClientArticleQueryService;
import com.vk.common.core.domain.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 用户服务降级处理
 * 
 * @author vk
 */
@Component
public class RemoteClientArticleQueryFallbackFactory implements FallbackFactory<RemoteClientArticleQueryService>
{
    private static final Logger log = LoggerFactory.getLogger(RemoteClientArticleQueryFallbackFactory.class);

    @Override
    public RemoteClientArticleQueryService create(Throwable throwable)
    {
        log.error("用户服务调用失败:{}", throwable.getMessage());
        return new RemoteClientArticleQueryService()
        {
            @Override
            public R<ApArticle> getOneArticle(Long id ) {
                return R.fail("查询文章存在失败"+throwable.getMessage());
            }
        };
    }
}
