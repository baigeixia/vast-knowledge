package com.vk.article.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.vk.analyze.domain.AdChannel;
import com.vk.analyze.feign.RemoteChannelService;
import com.vk.article.domain.ApArticle;
import com.vk.article.domain.ApArticleConfig;
import com.vk.article.domain.vo.ArticleInfoVo;
import com.vk.article.mapper.ApArticleConfigMapper;
import com.vk.article.mapper.ApArticleContentMapper;
import com.vk.article.mapper.ApArticleMapper;
import com.vk.article.service.ApArticleService;
import com.vk.common.core.context.SecurityContextHolder;
import com.vk.common.core.domain.R;
import com.vk.common.core.exception.LeadNewsException;
import com.vk.common.core.utils.StringUtils;
import com.vk.db.repository.article.ArticleMgRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

/**
 * 已发布的文章信息 服务层实现。
 *
 * @author 张三
 * @since 2024-07-11
 */
@Service
@Slf4j
public class ApArticleServiceImpl extends ServiceImpl<ApArticleMapper, ApArticle> implements ApArticleService {


    @Autowired
    private ArticleMgRepository articleMgRepository;

    @Autowired
    private ApArticleContentMapper articleContentMapper;

    @Autowired
    private ApArticleConfigMapper apArticleConfigMapper;

    @Autowired
    private RemoteChannelService remoteChannelService;

    @Override
    public Long saveArticle(ApArticle article) {
        Long id = article.getId();
        String title = article.getTitle();
        Long channelId = article.getChannelId();
        String channelName = article.getChannelName();
        String labels = article.getLabels();
        String image = article.getImages();
        if (StringUtils.isEmpty(title)) {
            throw new LeadNewsException("标题不能为空");
        }
        Long userId = SecurityContextHolder.getUserId();
        if (null == userId || userId == 0L) {
            throw new LeadNewsException(401, "未登录");
        }
        article.setAuthorId(userId);


        // 首次
        if (!ObjectUtils.isEmpty(channelId)) {
            // 查询频道
            R<AdChannel> channel = remoteChannelService.getChannel(channelId);
            if (StringUtils.isNull(channel) || StringUtils.isNull(channel.getData())){
                throw new LeadNewsException("频道不存在");
            }
            article.setChannelName(channelName);
        }

        if (!ObjectUtils.isEmpty(labels)) {
            article.setLabels(labels);
        }
        // image 图片处理 查上传库 是否有对应的封面图片
        if (!ObjectUtils.isEmpty(image)) {
            article.setImages(image);
        }

        try {
            mapper.insertOrUpdateSelective(article);
        } catch (Exception e) {
            log.error("保存文章信息失败 : {}", e.getMessage());
        }

        return id;
    }

    @Override
    public ArticleInfoVo infoArticle(Long articleId) {
        if (articleId == null || articleId==0L){
            throw new LeadNewsException("文章id不能为空");
        }

        ApArticle article = mapper.selectOneById(articleId);
        ArticleInfoVo articleInfoVo = new ArticleInfoVo();
        if (article!= null) {
            articleInfoVo.setId(article.getId());
            articleInfoVo.setTitle(article.getTitle());
            articleInfoVo.setChannelId(article.getChannelId());
            articleInfoVo.setChannelName(article.getChannelName());
            articleInfoVo.setLabels(article.getLabels());
            articleInfoVo.setImages(article.getImages());

            ApArticleConfig config = apArticleConfigMapper.selectOneById(articleId);
            if (config!= null) {
                articleInfoVo.setIsComment(config.getIsComment());
                articleInfoVo.setIsForward(config.getIsForward());
                articleInfoVo.setIsDown(config.getIsDown());
                articleInfoVo.setIsDelete(config.getIsDelete());
            }else {
                log.error("文章配置信息为空");
            }
        }else {
            log.error("文章信息为空");
        }
        return articleInfoVo;
    }
}
