package com.vk.article.service.impl;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.row.Db;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.vk.analyze.domain.AdChannel;
import com.vk.analyze.feign.RemoteChannelService;
import com.vk.article.domain.ApArticle;
import com.vk.article.domain.ApArticleConfig;
import com.vk.common.es.domain.ArticleInfoDocument;
import com.vk.article.domain.HomeArticleListVo;
import com.vk.article.domain.dto.ArticleAndConfigDto;
import com.vk.article.domain.vo.ArticleInfoVo;
import com.vk.article.domain.vo.ArticleListVo;
import com.vk.article.mapper.ApArticleConfigMapper;
import com.vk.article.mapper.ApArticleContentMapper;
import com.vk.article.mapper.ApArticleLabelMapper;
import com.vk.article.mapper.ApArticleMapper;
import com.vk.common.es.repository.ArticleDocumentRepository;
import com.vk.article.service.ApArticleService;
import com.vk.common.core.context.SecurityContextHolder;
import com.vk.common.core.domain.R;
import com.vk.common.core.domain.ValidationUtils;
import com.vk.common.core.exception.LeadNewsException;
import com.vk.common.core.utils.RequestContextUtil;
import com.vk.common.core.utils.StringUtils;
import com.vk.common.core.utils.threads.TaskVirtualExecutorUtil;
import com.vk.common.redis.service.RedisService;
import com.vk.db.domain.article.ArticleMg;
import com.vk.db.repository.article.ArticleMgRepository;
import com.vk.user.domain.AuthorInfo;
import com.vk.user.feign.RemoteClientUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
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
    private ArticleDocumentRepository articleDocumentRepository;



    @Autowired
    private RedisService redisService;

    @Autowired
    private RemoteClientUserService remoteClientUserService;

    @Override
    public Long saveArticle(ApArticle article) {
        Long userId = SecurityContextHolder.getUserId();
        if (null == userId || userId == 0L) {
            throw new LeadNewsException(401, "未登录");
        }
        // String userName = SecurityContextHolder.getUserName();
        // article.setAuthorName(userName);

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


        if (!ObjectUtils.isEmpty(labels)) {
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
            Long authorId = article.getAuthorId();
            R<Map<Long, AuthorInfo>> userList = remoteClientUserService.getUserList(Set.of(authorId));
            ValidationUtils.validateR(userList, "错误的用户");
            Map<Long, AuthorInfo> data = userList.getData();

            if (null != data) articleInfoVo.setAuthorName(data.get(authorId).getUsername());

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
        // String userName = RequestContextUtil.getUserName();
        // article.setAuthorName(userName);

        article.setLabels(dto.getLabels());
        article.setUpdateTime(dateTime);

        ApArticleConfig dbConfig = apArticleConfigMapper.selectOneByQuery(QueryWrapper.create().where(AP_ARTICLE_CONFIG.ARTICLE_ID.eq(articleId)));

        Db.tx(() -> {
            mapper.insertOrUpdateSelective(article);


            ApArticleConfig config = new ApArticleConfig();
            ApArticleConfig dtoConfig = dto.getConfig();
            BeanUtils.copyProperties(dtoConfig, config);
            if (null!=dbConfig){
                config.setId(dbConfig.getId());
            }
            config.setArticleId(article.getId());
            if (null == dtoConfig.getIsDown()) dtoConfig.setIsDown(false);
            if (null == dtoConfig.getIsDelete()) dtoConfig.setIsDelete(false);

            apArticleConfigMapper.insertOrUpdateSelective(config);

            return true;
        });

        return article.getId();
    }

    @Override
    public Page<HomeArticleListVo> listArticle(Long page, Long size, Integer tag, Integer type) {

        Page<HomeArticleListVo> listVoPage;

        QueryWrapper wrapper = QueryWrapper.create();
        if (null != tag && tag != 0) {
            wrapper.where(AP_ARTICLE.CHANNEL_ID.eq(tag));
        }
        // type 排序
        if (type == 0) {
            // 推荐
            wrapper.select().orderBy(AP_ARTICLE.LIKES, false);
        }

        if (type == 1) {
            // 最新
            wrapper.select().orderBy(AP_ARTICLE.CREATED_TIME, false);
        }

        listVoPage = mapper.paginateAs(Page.of(page, size), wrapper, HomeArticleListVo.class);

        List<HomeArticleListVo> records = listVoPage.getRecords();
        getMongoDescription(records);
        return listVoPage;
    }

    private void getMongoDescription(List<HomeArticleListVo> listVoPage) {
        if (null != listVoPage) {
            try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
                executor.submit(() -> {
                    Set<Long> authorIds = listVoPage.stream().map(HomeArticleListVo::getAuthorId).collect(Collectors.toSet());
                    R<Map<Long, AuthorInfo>> userList = remoteClientUserService.getUserList(authorIds);
                    ValidationUtils.validateR(userList, "错误的用户");

                    for (HomeArticleListVo record : listVoPage) {
                        ArticleMg articleMg = articleMgRepository.findByArticleId(record.getId());
                        record.setSimpleDescription(articleMg.getSimpleDescription());
                        Map<Long, AuthorInfo> infoMap = userList.getData();
                        if (!ObjectUtils.isEmpty(infoMap)) {
                            record.setAuthorName(infoMap.get(record.getAuthorId()).getUsername());
                        }
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

                List<HomeArticleListVo> homeArticleListVos = apArticles.stream().map(i -> {
                    HomeArticleListVo listVo = new HomeArticleListVo();
                    BeanUtils.copyProperties(i, listVo);
                    return listVo;
                }).toList();

                getMongoDescription(homeArticleListVos);

                return homeArticleListVos.stream().collect(Collectors.toMap(HomeArticleListVo::getId, listVo -> listVo, (existingValue, newValue) -> newValue));
            });
        }

        return listVoMap;
    }

    @Override
    public Map<Long, HomeArticleListVo> getBehaviorArticleIdList(Long userId, Set<Long> ids, Long page) {
        if (!StringUtils.isLongEmpty(userId)) {
            List<Long> articleIds = mapper.selectUserIdGetList(userId, page);
            ids.addAll(articleIds);
        }
        return getArticleIdList(ids);
    }

    @Override
    public Page<HomeArticleListVo> userArticleList(Long page, Long size, Integer type, Long userId) {
        if (StringUtils.isLongEmpty(userId)) {
            userId = RequestContextUtil.getUserId();
        }
        QueryWrapper wrapper = QueryWrapper.create();
        wrapper.
                select(AP_ARTICLE.DEFAULT_COLUMNS).from(AP_ARTICLE)
                .leftJoin(AP_ARTICLE_CONFIG).on(AP_ARTICLE_CONFIG.ARTICLE_ID.eq(AP_ARTICLE.ID))
                .where(AP_ARTICLE.AUTHOR_ID.eq(userId)).and(AP_ARTICLE_CONFIG.IS_DELETE.eq(0))
                .and(AP_ARTICLE_CONFIG.IS_DOWN.eq(0));

        if (type == 1) {
            wrapper.orderBy(AP_ARTICLE.CREATED_TIME, false);
        } else if (type == 2) {
            wrapper.orderBy(AP_ARTICLE.LIKES, false);
        } else {
            throw new LeadNewsException("错误的参数");
        }

        Page<HomeArticleListVo> homeArticleListVoPage = mapper.paginateAs(Page.of(page, size), wrapper, HomeArticleListVo.class);
        getMongoDescription(homeArticleListVoPage.getRecords());
        // for (HomeArticleListVo record : homeArticleListVoPage.getRecords()) {
        //     ArticleMg articleMg = articleMgRepository.findByArticleId(record.getId());
        //     record.setSimpleDescription(articleMg.getSimpleDescription());
        // }
        return homeArticleListVoPage;
    }

    @Override
    public List<HomeArticleListVo> getSearchArticleList(String query, Integer type, Integer sort, Integer period, Long page, Long size) {

        return null;
    }

    @Override
    public Long selectCount(LocalDateTime now) {
        return mapper.selectCount(now);
    }

    @Override
    @Async("asyncTaskExecutor")
    public void importAll(long page, Long size, CountDownLatch countDownLatch, LocalDateTime now) {
        String threadName = Thread.currentThread().getName();
        log.info("{} start: page={},size={}", threadName,page,size);
        //1分页查询到数据
        List<ArticleInfoDocument> articleList = mapper.selectByPage((page - 1) * size, size,now);
        log.info("{} start: page={},size={}, actualSize={} found", threadName,page,size,articleList.size());
        //2.分批导入到ES中
        try {
            articleDocumentRepository.saveAll(articleList);
            log.info("{} end: page={},size={}, actualSize={} found", threadName,page,size,articleList.size());
        } catch (Exception e) {
            log.error("{} error: page={},size={}, actualSize={} found", threadName,page,size,articleList.size(),e);
        } finally {
            //减掉数量
            countDownLatch.countDown();
        }

    }
}
