package com.vk.article.service.impl;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.row.Db;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.vk.article.domain.ApArticle;
import com.vk.article.domain.ApArticleContent;
import com.vk.article.domain.dto.SaveArticleContentDto;
import com.vk.article.mapper.ApArticleContentMapper;
import com.vk.article.mapper.ApArticleMapper;
import com.vk.article.service.ApArticleContentService;
import com.vk.common.core.exception.LeadNewsException;
import com.vk.common.core.utils.RequestContextUtil;
import com.vk.common.core.utils.StringUtils;
import com.vk.db.domain.article.ArticleMg;
import com.vk.db.repository.article.ArticleMgRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;

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

    @Autowired
    private ApArticleMapper apArticleMapper;

    @Override
    public Long contentSave(SaveArticleContentDto dto) {
        Long articleId = dto.getArticleId();

        Long userId = RequestContextUtil.getUserId();
        dto.setAuthorId(userId);


        if (ObjectUtils.isEmpty(articleId)) {
            // 第一次 保存
            initContentInset(dto);
            return dto.getArticleId();
        }

        ArticleMg articleMg = articleMgRepository.findByArticleId(articleId);
        if (null == articleMg) {
            // MongoDB 没数据 看下数据库中是否有
            ApArticleContent articleContent = mapper.selectOneByQuery(
                    QueryWrapper.create()
                            .where(AP_ARTICLE_CONTENT.ARTICLE_ID.eq(articleId)));
            if (null == articleContent) {
                // 数据库中也没有 走第一次保存
                initContentInset(dto);
            } else {
                ArticleMg sevenMg = new ArticleMg();
                BeanUtils.copyProperties(dto, sevenMg);

                articleMgRepository.insert(sevenMg);
            }
        } else {
            ArticleMg sevenMg = new ArticleMg();
            BeanUtils.copyProperties(dto, sevenMg);
            sevenMg.setId(articleMg.getId());

            try {
                articleMgRepository.save(sevenMg);
            } catch (Exception e) {
                log.error("更新 MongoDB 失败 文章详情id ： {} error ：{}", dto.getId(), e.getMessage());
            }
        }

        return dto.getArticleId();
    }


    @Override
    public ApArticleContent getInfoContent(Long articleId) {

        if (StringUtils.isLongEmpty(articleId)) {
            throw new LeadNewsException("id不能为空");
        }
        ApArticleContent articleContent = new ApArticleContent();

        ArticleMg articleMg = articleMgRepository.findByArticleId(articleId);
        if (null != articleMg) {
            BeanUtils.copyProperties(articleMg, articleContent);
            return articleContent;
        }


        return mapper.selectOneByQuery(QueryWrapper.create().where(AP_ARTICLE_CONTENT.ARTICLE_ID.eq(articleId)));
    }

    private void initContentInset(ApArticleContent insetContent) {
        Long userId = RequestContextUtil.getUserId();
        String userName = RequestContextUtil.getUserName();
        LocalDateTime dateTime = LocalDateTime.now();
        Db.tx(() -> {
            // 文章表初始化
            ApArticle article = new ApArticle();
            article.setAuthorId(userId);
            article.setAuthorName(userName);
            article.setUpdateTime(dateTime);
            article.setCreatedTime(dateTime);
            apArticleMapper.insertSelective(article);

            insetContent.setArticleId(article.getId());
            mapper.insertSelective(insetContent);

            return true;
        });

        // 初始化MongoDB
        ArticleMg sevenMg = new ArticleMg();
        BeanUtils.copyProperties(insetContent, sevenMg);

        try {
            articleMgRepository.insert(sevenMg);
        } catch (Exception e) {
            log.error("更新 MongoDB 失败 文章详情id ： {} error ：{}", insetContent.getId(), e.getMessage());
        }

    }
}
