package com.vk.article.service.impl;

import com.alibaba.nacos.common.utils.UuidUtils;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.vk.article.domain.ApArticleContent;
import com.vk.article.mapper.ApArticleContentMapper;
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

    @Override
    public Long contentSave(ApArticleContent apArticleContent) {
        Long id = apArticleContent.getId();
        String content = apArticleContent.getContent();

        if (ObjectUtils.isEmpty(content)) {
            throw new LeadNewsException("错误的参数");
        }

        Long userId = RequestContextUtil.getUserId();
        apArticleContent.setAuthorId(userId);

        if (ObjectUtils.isEmpty(id)) {
            contentInset(apArticleContent);
            return apArticleContent.getId();
        }

        ArticleMg articleMg = articleMgRepository.findById(id).orElse(null);
        if (null == articleMg) {
            ApArticleContent articleContent = mapper.selectOneById(id);
            if (null == articleContent){
                contentInset(apArticleContent);
            }
        } else {
            ArticleMg sevenMg = new ArticleMg();
            BeanUtils.copyProperties(apArticleContent, sevenMg);
            try {
                articleMgRepository.save(sevenMg);
            } catch (Exception e) {
                log.error("更新 MongoDB 失败 文章详情id ： {} error ：{}",apArticleContent.getId(),e.getMessage());
            }
        }

        return  apArticleContent.getId();
    }

    @Override
    public ApArticleContent getInfoContent(Long id) {

        if (StringUtils.isLongEmpty(id)){
            throw new LeadNewsException("id不能为空");
        }

        ArticleMg articleMg = articleMgRepository.findById(id).orElse(null);
        if (null!= articleMg){
            ApArticleContent articleContent = new ApArticleContent();
            BeanUtils.copyProperties(articleMg,articleContent);
            return articleContent;
        }

        return mapper.selectOneById(id);
    }

    private  void  contentInset(ApArticleContent  insetContent  ){

        int inserted = mapper.insertSelective(insetContent);
        if (inserted == 1) {
            ArticleMg sevenMg = new ArticleMg();
            BeanUtils.copyProperties(insetContent, sevenMg);
            try {
                articleMgRepository.insert(sevenMg);
            } catch (Exception e) {
                log.error("更新 MongoDB 失败 文章详情id ： {} error ：{}",insetContent.getId(),e.getMessage());
            }
        }else {
            log.error("插入 数据库 失败 文章详情id ： {}",insetContent.getId());
        }
    }
}
