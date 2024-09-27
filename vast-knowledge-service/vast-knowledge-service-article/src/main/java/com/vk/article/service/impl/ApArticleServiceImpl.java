package com.vk.article.service.impl;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.row.Db;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.vk.analyze.domain.AdChannel;
import com.vk.analyze.feign.RemoteChannelService;
import com.vk.article.domain.ApArticle;
import com.vk.article.domain.ApArticleConfig;
import com.vk.article.domain.HomeArticleListVo;
import com.vk.article.domain.dto.ArticleAndConfigDto;
import com.vk.article.domain.table.ApArticleConfigTableDef;
import com.vk.article.domain.table.ApArticleContentTableDef;
import com.vk.article.domain.table.ApArticleTableDef;
import com.vk.article.domain.vo.ArticleInfoVo;
import com.vk.article.domain.vo.ArticleListVo;
import com.vk.article.mapper.ApArticleConfigMapper;
import com.vk.article.mapper.ApArticleContentMapper;
import com.vk.article.mapper.ApArticleLabelMapper;
import com.vk.article.mapper.ApArticleMapper;
import com.vk.article.service.ApArticleService;
import com.vk.common.core.context.SecurityContextHolder;
import com.vk.common.core.domain.R;
import com.vk.common.core.exception.LeadNewsException;
import com.vk.common.core.utils.RequestContextUtil;
import com.vk.common.core.utils.StringUtils;
import com.vk.common.core.utils.threads.TaskVirtualExecutorUtil;
import com.vk.common.redis.service.RedisService;
import com.vk.db.domain.article.ArticleMg;
import com.vk.db.repository.article.ArticleMgRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static com.vk.article.domain.table.ApArticleConfigTableDef.AP_ARTICLE_CONFIG;
import static com.vk.article.domain.table.ApArticleTableDef.AP_ARTICLE;
import static com.vk.common.redis.constants.BusinessConstants.loadingChannel;

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

    @Autowired
    private ApArticleLabelMapper apArticleLabelMapper;


    @Autowired
    private RedisService redisService;

    @Override
    public Long saveArticle(ApArticle article) {
        Long userId = SecurityContextHolder.getUserId();
        if (null == userId || userId == 0L) {
            throw new LeadNewsException(401, "未登录");
        }
        String userName = SecurityContextHolder.getUserName();
        article.setAuthorName(userName);

        Long id = article.getId();
        String title = article.getTitle();
        Long channelId = article.getChannelId();
        String labels = article.getLabels();
        String image = article.getImages();

        if (StringUtils.isEmpty(title)) {
            throw new LeadNewsException("标题不能为空");
        }

        article.setAuthorId(userId);
        if (!ObjectUtils.isEmpty(channelId)) {
            var channelName = "";
            AdChannel cacheList = redisService.getCacheObject(loadingChannel(channelId));
            if (ObjectUtils.isEmpty(cacheList)) {
                R<AdChannel> channel = remoteChannelService.getChannel(channelId);
                if (StringUtils.isNull(channel) || StringUtils.isNull(channel.getData())) {
                    throw new LeadNewsException("频道不存在");
                } else {
                    channelName = channel.getData().getName();
                }
            } else {
                channelName = cacheList.getName();
            }
            // 查询频道
            article.setChannelName(channelName);
        }


        // List <ApArticleLabel> insetLabel=new ArrayList<>();
        if (!ObjectUtils.isEmpty(labels)) {
            // List<String> labelID = List.of(labels.split(","));
            // 将字符串数组转换为 Long 类型的 List
            // List<Long> labelID = Arrays.stream(labels.split(","))
            //         .map(Long::valueOf)
            //         .toList();
            //
            // String labelNames = labelID.stream()
            //         .map(i -> {
            //             AdChannel cacheList = redisService.getCacheObject(loadingLabel(i), AdChannel.class);
            //             if (!ObjectUtils.isEmpty(cacheList)) {
            //                 ApArticleLabel articleLabel = new ApArticleLabel();
            //                 articleLabel.setArticleId(i);
            //                 articleLabel.setLabelId(cacheList.getId());
            //                 insetLabel.add(articleLabel);
            //                 // 返回标签名称
            //                 return cacheList.getName();
            //             } else {
            //                 return ""; // 或者返回其他默认值，视情况而定
            //             }
            //         })
            //         .collect(Collectors.joining(", "));
            //
            // article.setLabels(labelNames);
            article.setLabels(labels);
        }
        // image 图片处理 查上传库 是否有对应的封面图片
        if (!ObjectUtils.isEmpty(image)) {
            article.setImages(image);
        }

        try {
            Db.tx(() -> {
                mapper.insertOrUpdateSelective(article);
                // if (StringUtils.isNotEmpty(insetLabel)){
                //     apArticleLabelMapper.insertBatch(insetLabel);
                // }

                return true;
            });
        } catch (Exception e) {
            log.error("保存文章信息失败 : {}", e.getMessage());
        }

        return id;
    }

    @Override
    public ArticleInfoVo infoArticle(Long articleId) {
        if (articleId == null || articleId == 0L) {
            throw new LeadNewsException("文章id不能为空");
        }

        ApArticle article = mapper.selectOneById(articleId);
        ArticleInfoVo articleInfoVo = new ArticleInfoVo();
        if (article != null) {
            BeanUtils.copyProperties(article, articleInfoVo);
            // articleInfoVo.setId(article.getId());
            // articleInfoVo.setTitle(article.getTitle());
            // articleInfoVo.setChannelId(article.getChannelId());
            // articleInfoVo.setChannelName(article.getChannelName());
            // articleInfoVo.setLabels(article.getLabels());
            // articleInfoVo.setImages(article.getImages());

            ApArticleConfig config = apArticleConfigMapper.selectOneById(articleId);
            if (config != null) {
                articleInfoVo.setConfig(config);
            } else {
                log.error("文章配置信息为空");
            }
        } else {
            log.error("文章信息为空");
        }
        return articleInfoVo;
    }

    @Override
    public Long saveOrUpArticle(ArticleAndConfigDto dto) {
        Long channelId = dto.getChannelId();
        Long articleId = dto.getArticleId();

        LocalDateTime dateTime = LocalDateTime.now();

        ApArticle article = new ApArticle();
        article.setImages(dto.getImages());
        article.setTitle(dto.getTitle());

        if (StringUtils.isLongEmpty(articleId)) {
            article.setCreatedTime(dateTime);
        } else {
            article.setId(articleId);
        }

        if (!StringUtils.isLongEmpty(channelId)) {
            article.setChannelId(channelId);
            AdChannel cacheList = redisService.getCacheObject(loadingChannel(channelId));
            article.setChannelName(cacheList.getName());
        }

        Long userId = RequestContextUtil.getUserId();
        article.setAuthorId(userId);
        String userName = RequestContextUtil.getUserName();
        article.setAuthorName(userName);

        article.setLabels(dto.getLabels());
        article.setUpdateTime(dateTime);

        Db.tx(() -> {
            mapper.insertOrUpdateSelective(article);

            ApArticleConfig config = new ApArticleConfig();
            ApArticleConfig dtoConfig = dto.getConfig();
            BeanUtils.copyProperties(dtoConfig, config);
            config.setArticleId(article.getId());
            if (null==dtoConfig.getIsDown()) dtoConfig.setIsDown(false);
            if (null==dtoConfig.getIsDelete()) dtoConfig.setIsDelete(false);

            apArticleConfigMapper.insertOrUpdateSelective(config);

            return true;
        });

        return article.getId();
    }

    @Override
    public Page<HomeArticleListVo> listArticle(Long page, Long size, Integer tag) {

        Page<HomeArticleListVo> listVoPage;
        // tag 标签
        if (tag == 0) {
            QueryWrapper wrapper = QueryWrapper.create();
            // 推荐
            listVoPage = mapper.paginateAs(Page.of(page, size), wrapper, HomeArticleListVo.class);
            getMongoDescription(listVoPage.getRecords());

            return listVoPage;
        }

        if (tag == 1) {
            // 最新
            QueryWrapper wrapper = QueryWrapper.create();
            wrapper.select().orderBy(AP_ARTICLE.CREATED_TIME, false);
            listVoPage = mapper.paginateAs(Page.of(page, size), wrapper, HomeArticleListVo.class);
            getMongoDescription(listVoPage.getRecords());

            return listVoPage;
        }


        QueryWrapper where = QueryWrapper.create();
        where.where(AP_ARTICLE.CHANNEL_ID.eq(tag));
        listVoPage = mapper.paginateAs(Page.of(page, size), where, HomeArticleListVo.class);

        List<HomeArticleListVo> records = listVoPage.getRecords();
        getMongoDescription(records);
        return listVoPage;
    }

    private void getMongoDescription(List<HomeArticleListVo> listVoPage) {
        if (null != listVoPage) {
            try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
                executor.submit(() -> {
                    for (HomeArticleListVo record : listVoPage) {
                        ArticleMg articleMg = articleMgRepository.findByArticleId(record.getId());
                        record.setSimpleDescription(articleMg.getSimpleDescription());
                        // record.setSimpleDescription(articleMg.getContent().length() > 500 ? articleMg.getContent().substring(0,500) : articleMg.getContent());
                        // record.setSimpleDescription( EscapeUtil.clean(articleMg.getContent().length() > 500 ? articleMg.getContent().substring(0,500) : articleMg.getContent()));
                    }
                });
            } catch (Exception e) {
                log.error("获取文章描述信息失败 : {}", e.getMessage());
            }
        }
    }

    @Override
    public Page<ArticleListVo> articleListArticle(
            Long page, Long size, Integer status, String title, Long channelId, String startTime, String endTime
    ) {
        Long userId = RequestContextUtil.getUserId();
        if (StringUtils.isLongEmpty(userId)) {
            throw new LeadNewsException(401, "未登录");
        }

        QueryWrapper wrapper = QueryWrapper.create();
        wrapper.where(AP_ARTICLE.AUTHOR_ID.eq(userId))
                .and(AP_ARTICLE.TITLE.like(title, StringUtils.isNotEmpty(title)))
                .and(AP_ARTICLE.CHANNEL_ID.eq(channelId, !StringUtils.isLongEmpty(channelId)))
                .and(AP_ARTICLE.CREATED_TIME.between(startTime, endTime, !ObjectUtils.isEmpty(startTime) && !ObjectUtils.isEmpty(endTime)));

        return mapper.paginateAs(Page.of(page, size), wrapper, ArticleListVo.class);
    }

    @Override
    public Map<Long, String> getArticleTitle(Set<Long> ids) {
        if (!ObjectUtils.isEmpty(ids)) {
            Map<Long, String> map = new HashMap<>();
            List<ApArticle> apArticles = mapper.selectListByIds(ids);
            for (ApArticle apArticle : apArticles) {
                String title = apArticle.getTitle();
                map.put(apArticle.getId(), title);
            }

            return map;
        }
        return null;
    }

    @Override
    public Map<Long, HomeArticleListVo> getArticleIdList(Set<Long> ids) {
        Map<Long, HomeArticleListVo> listVoMap = new HashMap<>();
        if (!ObjectUtils.isEmpty(ids)) {
            listVoMap = TaskVirtualExecutorUtil.executeWith(() -> {
                List<ApArticle> apArticles = mapper.selectListByIds(ids);

                return apArticles.stream().map(i -> {
                    HomeArticleListVo listVo = new HomeArticleListVo();
                    BeanUtils.copyProperties(i, listVo);
                    ArticleMg articleMg = articleMgRepository.findByArticleId(i.getId());
                    listVo.setSimpleDescription(articleMg.getSimpleDescription());
                    return listVo;
                }).collect(Collectors.toMap(HomeArticleListVo::getId, listVo -> listVo, (existingValue, newValue) -> newValue));
            });
        }

        return listVoMap;
    }

    @Override
    public Map<Long, HomeArticleListVo> getBehaviorArticleIdList(Long userId,Set<Long> ids,Long page) {
        if (!StringUtils.isLongEmpty(userId)){
           List<Long> articleIds= mapper.selectUserIdGetList(userId,page);
            ids.addAll(articleIds);
        }
        return getArticleIdList(ids);
    }

    @Override
    public Page<HomeArticleListVo> userArticleList(Long page, Long size,Integer type, Long userId) {
        if (StringUtils.isLongEmpty(userId)) {
            userId = RequestContextUtil.getUserId();
        }
        QueryWrapper wrapper = QueryWrapper.create();
        wrapper.
                select(AP_ARTICLE.DEFAULT_COLUMNS).from(AP_ARTICLE)
                .leftJoin(AP_ARTICLE_CONFIG).on(AP_ARTICLE_CONFIG.ARTICLE_ID.eq(AP_ARTICLE.ID))
                .where(AP_ARTICLE.AUTHOR_ID.eq(userId)).and(AP_ARTICLE_CONFIG.IS_DELETE.eq(0))
                .and(AP_ARTICLE_CONFIG.IS_DOWN.eq(0));

        if (type==1){
            wrapper.orderBy(AP_ARTICLE.CREATED_TIME,false);
        }else if (type==2){
            wrapper.orderBy(AP_ARTICLE.LIKES,false);
        }else {
            throw new LeadNewsException("错误的参数");
        }

        Page<HomeArticleListVo> homeArticleListVoPage = mapper.paginateAs(Page.of(page, size), wrapper, HomeArticleListVo.class);
        for (HomeArticleListVo record : homeArticleListVoPage.getRecords()) {
            ArticleMg articleMg = articleMgRepository.findByArticleId(record.getId());
            record.setSimpleDescription(articleMg.getSimpleDescription());
        }
        return homeArticleListVoPage;
    }
}
