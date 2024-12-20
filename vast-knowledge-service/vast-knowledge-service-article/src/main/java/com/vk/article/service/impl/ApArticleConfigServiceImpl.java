package com.vk.article.service.impl;

import com.mybatisflex.core.row.Db;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.vk.article.domain.ApArticleConfig;
import com.vk.article.domain.ApArticleContent;
import com.vk.article.mapper.ApArticleConfigMapper;
import com.vk.article.mapper.ApArticleContentMapper;
import com.vk.article.mapper.ApArticleMapper;
import com.vk.article.service.ApArticleConfigService;
import com.vk.common.core.exception.LeadNewsException;
import com.vk.common.core.utils.RequestContextUtil;
import com.vk.common.core.utils.StringUtils;
import com.vk.common.es.repository.ArticleDocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * APP已发布文章配置 服务层实现。
 *
 * @author 张三
 * @since 2024-07-11
 */
@Service
public class ApArticleConfigServiceImpl extends ServiceImpl<ApArticleConfigMapper, ApArticleConfig> implements ApArticleConfigService {

    @Autowired
    private ApArticleMapper apArticleMapper;

    @Autowired
    private ApArticleContentMapper articleContentMapper;

    @Autowired
    private ArticleDocumentRepository articleDocumentRepository;
    @Override
    public void deleteArticle(Long id) {
        if (StringUtils.isLongEmpty(id)){
            throw new LeadNewsException("错误的文章id");
        }
        Long userId = RequestContextUtil.getUserId();
       Long count= apArticleMapper.userArticle(userId,id);
       if (StringUtils.isLongEmpty(count)){
           throw new LeadNewsException("文章错误 不存在");
       }
        Db.tx(()->{
            apArticleMapper.deleteById(id);
            articleContentMapper.deleteById(id);
            mapper.deleteById(id);

            articleDocumentRepository.deleteById(id);

            return true;
        });


    }
}
