package com.vk.article.service.impl;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.vk.article.domain.ApArticleContent;
import com.vk.article.mapper.ApArticleContentMapper;
import com.vk.article.service.ApArticleContentService;
import com.vk.common.core.exception.LeadNewsException;
import com.vk.db.domain.article.ArticleMg;
import com.vk.db.repository.article.ArticleMgRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import static com.vk.article.domain.table.ApArticleContentTableDef.AP_ARTICLE_CONTENT;

/**
 * APP已发布文章内容 服务层实现。
 *
 * @author 张三
 * @since 2024-07-11
 */
@Service
@Slf4j
public class ApArticleContentServiceImpl extends ServiceImpl<ApArticleContentMapper, ApArticleContent> implements ApArticleContentService {


    @Autowired
    private ArticleMgRepository articleMgRepository;

    @Override
    public void contentSave(ApArticleContent apArticleContent) {
        Long id = apArticleContent.getId();
        Long articleId = apArticleContent.getArticleId();
        String content = apArticleContent.getContent();


        if (ObjectUtils.isEmpty(articleId)) {
            throw new LeadNewsException("错误的参数");
        }
        if (ObjectUtils.isEmpty(content)) {
            throw new LeadNewsException("错误的参数");
        }

        if (null == id) {
            // 保存
            ApArticleContent articleContent = mapper.selectOneByQuery(QueryWrapper.create().where(AP_ARTICLE_CONTENT.ARTICLE_ID.eq(articleId)));
            if (null != articleContent) {
                throw new LeadNewsException("错误的参数");
            }
        } else {
            ArticleMg articleMg = articleMgRepository.findById(id).orElse(null);
            if (null == articleMg) {
                ApArticleContent articleContent = mapper.selectOneById(id);
                if (null == articleContent) {
                     if (mapper.insert(apArticleContent) == 1){
                         ArticleMg sevenMg = new ArticleMg();
                         BeanUtils.copyProperties(apArticleContent, sevenMg);
                         try {
                            articleMgRepository.insert(sevenMg);
                         } catch (Exception e) {
                             log.error("更新 MongoDB 失败 文章详情id ： {}",id);
                         }

                     }

                }

            }
        }

    }
}
