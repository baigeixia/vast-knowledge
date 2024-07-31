package com.vk.comment.common.utils;

import com.vk.article.domain.ApArticle;
import com.vk.article.feign.RemoteClientArticleQueryService;
import com.vk.common.core.constant.SecurityConstants;
import com.vk.common.core.domain.R;
import com.vk.common.core.exception.LeadNewsException;
import com.vk.common.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class CommentUtils {

    @Autowired
    private RemoteClientArticleQueryService remoteClientArticleQueryService;

    public Long getaLong(Long entryId, Long authorId, Long userId) {
        R<ApArticle> articleStatus = remoteClientArticleQueryService.getOneArticle(entryId);
        if (StringUtils.isNull(articleStatus) || StringUtils.isNull(articleStatus.getData())) {
            throw new LeadNewsException("错误的文章");
        }

        ApArticle article = articleStatus.getData();
        Long articleAuthorId = article.getAuthorId();

        if (!Objects.equals(authorId, userId) && !Objects.equals(authorId, articleAuthorId)) {
            // 不是发布人，不是文章作者，不能修改
            throw new LeadNewsException("错误的修改");
        }
        return articleAuthorId;
    }
}
