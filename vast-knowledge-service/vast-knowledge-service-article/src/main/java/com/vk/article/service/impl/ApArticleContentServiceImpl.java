package com.vk.article.service.impl;

import com.alibaba.nacos.common.utils.UuidUtils;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.row.Db;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.vk.article.domain.ApArticle;
import com.vk.article.domain.ApArticleContent;
import com.vk.article.domain.table.ApArticleContentTableDef;
import com.vk.article.mapper.ApArticleContentMapper;
import com.vk.article.mapper.ApArticleMapper;
import com.vk.article.service.ApArticleContentService;
import com.vk.common.core.context.SecurityContextHolder;
import com.vk.common.core.exception.LeadNewsException;
import com.vk.common.core.utils.RequestContextUtil;
import com.vk.common.core.utils.ServletUtils;
import com.vk.common.core.utils.StringUtils;
import com.vk.common.core.utils.uuid.UUID;
import com.vk.db.domain.article.ArticleMg;
import com.vk.db.repository.article.ArticleMgRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.List;

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
    public Long contentSave(ApArticleContent apArticleContent) {
        Long articleId = apArticleContent.getArticleId();

        Long userId = RequestContextUtil.getUserId();
        apArticleContent.setAuthorId(userId);

        if (ObjectUtils.isEmpty(articleId)) {
            //第一次 保存
            initContentInset(apArticleContent);
            return apArticleContent.getArticleId();
        }

        ArticleMg articleMg = articleMgRepository.findByArticleId(articleId);
        if (null == articleMg) {
            //MongoDB 没数据 看下数据库中是否有
            ApArticleContent articleContent = mapper.selectOneByQuery(
                    QueryWrapper.create()
                    .where(AP_ARTICLE_CONTENT.ARTICLE_ID.eq(articleId)));
            if (null == articleContent){
                //数据库中也没有 走第一次保存
                initContentInset(apArticleContent);
            }else {
                ArticleMg sevenMg = new ArticleMg();
                BeanUtils.copyProperties(apArticleContent, sevenMg);
                articleMgRepository.insert(sevenMg);
            }
        } else {
            ArticleMg sevenMg = new ArticleMg();
            BeanUtils.copyProperties(apArticleContent, sevenMg);
            sevenMg.setId(articleMg.getId());
            try {
                articleMgRepository.save(sevenMg);
            } catch (Exception e) {
                log.error("更新 MongoDB 失败 文章详情id ： {} error ：{}", apArticleContent.getId(),e.getMessage());
            }
        }

        return  apArticleContent.getArticleId();
    }

    private void saveMongoContent(ApArticleContent apArticleContent) {
        ArticleMg sevenMg = new ArticleMg();
        BeanUtils.copyProperties(apArticleContent, sevenMg);
        try {
            articleMgRepository.updateByArticleId(sevenMg.getArticleId(), sevenMg.getContent());
        } catch (Exception e) {
            log.error("更新 MongoDB 失败 文章详情id ： {} error ：{}", apArticleContent.getId(),e.getMessage());
        }
    }

    @Override
    public ApArticleContent getInfoContent(Long id) {

        if (StringUtils.isLongEmpty(id)){
            throw new LeadNewsException("id不能为空");
        }

        ArticleMg articleMg = articleMgRepository.findByArticleId(id);
        if (null!= articleMg){
            ApArticleContent articleContent = new ApArticleContent();
            BeanUtils.copyProperties(articleMg,articleContent);
            return articleContent;
        }

        return mapper.selectOneByQuery(
                QueryWrapper.create()
                        .where(AP_ARTICLE_CONTENT.ARTICLE_ID.eq(id)));
    }

    private  void  initContentInset(ApArticleContent  insetContent  ){
        Long userId = RequestContextUtil.getUserId();
        String userName = RequestContextUtil.getUserName();
        LocalDateTime dateTime = LocalDateTime.now();
        Db.tx(()->{
            //文章表初始化
            ApArticle article = new ApArticle();
            article.setAuthorId(userId);
            article.setAuthorName(userName);
            article.setUpdateTime(dateTime);
            article.setCreatedTime(dateTime);
            apArticleMapper.insertSelective(article);

            insetContent.setArticleId(article.getId());
            mapper.insertSelective(insetContent);

            return  true;
        });

        //初始化MongoDB
            ArticleMg sevenMg = new ArticleMg();
            BeanUtils.copyProperties(insetContent, sevenMg);
            try {
                articleMgRepository.insert(sevenMg);
            } catch (Exception e) {
                log.error("更新 MongoDB 失败 文章详情id ： {} error ：{}",insetContent.getId(),e.getMessage());
            }

    }
}
