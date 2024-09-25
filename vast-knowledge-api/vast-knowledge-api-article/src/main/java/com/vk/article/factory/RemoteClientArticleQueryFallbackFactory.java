package com.vk.article.factory;


import com.vk.article.domain.ApArticle;
import com.vk.article.domain.HomeArticleListVo;
import com.vk.article.feign.RemoteClientArticleQueryService;
import com.vk.common.core.domain.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

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

            @Override
            public R<Map<Long, String>> getArticleTitle(Set<Long> ids) {
                return R.fail("获取文章标题失败"+throwable.getMessage());
            }

            @Override
            public R<Map<Long, HomeArticleListVo>> getArticleIdList(Set<Long> ids) {
                return  R.fail("获取文章列表数据失败"+throwable.getMessage());
            }

            @Override
            public R<Map<Long, HomeArticleListVo>> getBehaviorArticleIdList(Long userId,  Long page,Set<Long> ids) {
                return  R.fail("获取用户动态列表数据失败"+throwable.getMessage());
            }
        };
    }
}
