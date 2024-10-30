package com.vk.user.service.impl;

import co.elastic.clients.json.JsonData;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.vk.common.core.exception.CustomSimpleThrowUtils;
import com.vk.common.core.exception.LeadNewsException;
import com.vk.common.core.utils.RequestContextUtil;
import com.vk.common.core.utils.StringUtils;
import com.vk.common.es.domain.UserInfoDocument;
import com.vk.common.es.repository.UserInfoDocumentRepository;
import com.vk.common.redis.service.RedisService;
import com.vk.user.domain.ApUser;
import com.vk.user.domain.ApUserInfo;
import com.vk.user.domain.dto.UserInfoDto;
import com.vk.user.domain.vo.InfoRelationVo;
import com.vk.user.domain.vo.SearchUserInfoVo;
import com.vk.user.domain.vo.UserInfoVo;
import com.vk.user.mapper.ApUserFollowMapper;
import com.vk.user.mapper.ApUserInfoMapper;
import com.vk.user.mapper.ApUserMapper;
import com.vk.user.service.ApUserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.HighlightQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.core.query.highlight.Highlight;
import org.springframework.data.elasticsearch.core.query.highlight.HighlightField;
import org.springframework.data.elasticsearch.core.query.highlight.HighlightParameters;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

import static com.vk.user.common.constant.UserConstants.redisUserInfoKey;
import static com.vk.user.domain.table.ApUserFollowTableDef.AP_USER_FOLLOW;

/**
 * APP用户详情信息 服务层实现。
 *
 * @author 张三
 * @since 2024-05-13
 */
@Service
@Slf4j
public class ApUserInfoServiceImpl extends ServiceImpl<ApUserInfoMapper, ApUserInfo> implements ApUserInfoService {

    @Autowired
    private RedisService redisService;
    @Autowired
    private ApUserMapper apUserMapper;

    @Autowired
    private ApUserFollowMapper apUserFollowMapper;

    @Autowired
    private UserInfoDocumentRepository userInfoDocumentRepository;

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;
    @Override
    public void userConfig(Integer state, Integer type) {
        if (state.equals(1)) {
            ApUserInfo info = userVerification();
            if (info.getIsSendMessage().equals(type)) {
                throw new LeadNewsException("请勿重复修改");
            }
            info.setIsSendMessage(type);
            mapper.update(info);

        } else if (state.equals(2)) {
            ApUserInfo info = userVerification();
            if (info.getIsRecommendMe().equals(type)) {
                throw new LeadNewsException("请勿重复修改");
            }
            info.setIsRecommendMe(type);
            mapper.update(info);
        } else if (state.equals(3)) {
            ApUserInfo info = userVerification();
            if (info.getIsRecommendFriend().equals(type)) {
                throw new LeadNewsException("请勿重复修改");
            }
            info.setIsRecommendFriend(type);
            mapper.update(info);

        } else if (state.equals(4)) {
            ApUserInfo info = userVerification();
            if (info.getIsDisplayImage().equals(type)) {
                throw new LeadNewsException("请勿重复修改");
            }
            info.setIsDisplayImage(type);
            mapper.update(info);
        } else {
            throw new LeadNewsException("错误的类型");
        }

        Long userid = RequestContextUtil.getUserId();
        redisService.deleteObject(redisUserInfoKey(userid));
        UserInfoVo userInfoVo = mapper.selectGetInfo(userid);
        redisService.setCacheObject(redisUserInfoKey(userid),userInfoVo);
    }

    @Override
    public UserInfoVo getInfo(Long id) {
        Long  userid = RequestContextUtil.getUserId();
        if (StringUtils.isLongEmpty(id)){
            return userInfoIdOne(userid);
        }else {
            return userInfoIdOne(id);
        }
    }

    @Override
    public void upInfo(UserInfoDto dto) {
        CustomSimpleThrowUtils.ObjectIsEmpty(dto, "错误的参数");

        Long id = dto.getId();
        if (null==id){
            throw  new LeadNewsException("缺失参数");
        }

        String name = dto.getName();
        CustomSimpleThrowUtils.StringIsEmpty(name,"名称不能为空");

        LocalDate birthday = dto.getBirthday();
        ApUserInfo upInfo = new ApUserInfo();
        LocalDate now = LocalDate.now();
        if (null!=birthday){
            Period period = Period.between( birthday,now);
            int years = period.getYears();
            upInfo.setAge(years);
        }

        // ApUser apUser = apUserMapper.selectOneById(id);
        // if (null==apUser){
        //     throw  new LeadNewsException("错误的用户");
        // }

        Long userid = RequestContextUtil.getUserId();
        ApUserInfo info = mapper.selectOneByUserId(id);

        if (null==info){
            upInfo.setSex(2);
            BeanUtils.copyProperties(dto,upInfo);
            upInfo.setUserId(userid);
            upInfo.setFans(0L);
            upInfo.setFollows(0L);
            upInfo.setIsDisplayImage(0);
            upInfo.setIsRecommendMe(0);
            upInfo.setIsSendMessage(0);
            upInfo.setIsRecommendFriend(0);
            upInfo.setUpdatedTime(LocalDateTime.now());
            mapper.insert(upInfo);
        }else {
            if (!info.getId().equals(id)){
                throw  new LeadNewsException("没有权限修改");
            }

            BeanUtils.copyProperties(dto,upInfo);
            upInfo.setUpdatedTime(LocalDateTime.now());
            mapper.update(upInfo);

        }

        redisService.deleteObject(redisUserInfoKey(userid));
    }

    @Override
    public InfoRelationVo InfoRelation(Long id) {
        Long userid = RequestContextUtil.getUserId();
        if (StringUtils.isLongEmpty(id)){
            id=userid;
        }

        if (id.equals(userid)){
            return new InfoRelationVo(false);
        }

        long countByQuery = apUserFollowMapper.selectCountByQuery(QueryWrapper.create().where(AP_USER_FOLLOW.USER_ID.eq(userid).and(AP_USER_FOLLOW.FOLLOW_ID.eq(id))));
        if (countByQuery>0){
            return new InfoRelationVo(true);
        }

        return new InfoRelationVo(false);
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
        List<UserInfoDocument> infoDocumentList = mapper.selectByPage((page - 1) * size, size,now);
        log.info("{} start: page={},size={}, actualSize={} found", threadName,page,size,infoDocumentList.size());
        //2.分批导入到ES中
        try {
            userInfoDocumentRepository.saveAll(infoDocumentList);
            log.info("{} end: page={},size={}, actualSize={} found", threadName,page,size,infoDocumentList.size());
        } catch (Exception e) {
            log.error("{} error: page={},size={}, actualSize={} found", threadName,page,size,infoDocumentList.size(),e);
        } finally {
            //减掉数量
            countDownLatch.countDown();
        }

    }


    /**
     * @param page   页数
     * @param size   长度
     * @param query  搜索内容
     * @param type   头部标题 4 用户
     * @param sort   排序    0 综合排序  1 最新  2 最热
     * @param period 时间    1 不限  2 最新一天  3 最近一周  4最近一月
     * @return
     */
    @Override
    public List<SearchUserInfoVo> searchUser(String query, Integer type, Integer sort, Integer period, Long page, Long size) {
        if (StringUtils.isEmpty(query)){
            throw new LeadNewsException("搜索内容不能为空");
        }
        if (type!=4){
            throw new LeadNewsException("搜索类型错误");
        }
        page = (page - 1);

        List<UserInfoDocument> userInfoDocuments = EsQueryUser(query, sort, period, page, size);
        Long localUserId = RequestContextUtil.getUserIdNotLogin();

        List<SearchUserInfoVo> searchUserInfoVos = new ArrayList<>();

        if (null==localUserId){
            //未登录
            for (UserInfoDocument infoDocument : userInfoDocuments) {
                SearchUserInfoVo vo = new SearchUserInfoVo();
                BeanUtils.copyProperties(infoDocument, vo);
                vo.setConcerned(0);

                searchUserInfoVos.add(vo);
            }
        }else {
            List<Long> userIds = userInfoDocuments.stream()
                    .map(UserInfoDocument::getId)
                    .toList();

            List<Long> followedUserIds =mapper.getFollowedUserIds(localUserId,userIds);
            for (UserInfoDocument infoDocument : userInfoDocuments) {
                SearchUserInfoVo vo = new SearchUserInfoVo();
                BeanUtils.copyProperties(infoDocument, vo);
                // 默认关注状态
                vo.setConcerned(followedUserIds.contains(infoDocument.getId()) ? 1 : 0);
                searchUserInfoVos.add(vo);
            }

        }

        return searchUserInfoVos;
    }

    private List<UserInfoDocument> EsQueryUser(String query, Integer sort, Integer period, Long page, Long size) {
        String pre = "<span style='color:red'>";
        String post = "</span>";

        LocalDateTime now = LocalDateTime.now();

        Query nativeQueryquery = NativeQuery.builder()
                .withQuery(q -> q
                        .bool(b -> {
                            b.should(s -> s.match(m -> m.field("name").query(query)));
                            //时间排序控制
                            switch (period) {
                                case 1:
                                    break;
                                case 2:
                                    String oneDayAgoStr = now.minusDays(1).atZone(ZoneId.systemDefault()).toInstant().toString();
                                    b.filter(f -> f.range(r -> r.field("createdTime").gte(JsonData.of(oneDayAgoStr))));
                                    break;
                                case 3:
                                    String oneWeekAgoStr = now.minusWeeks(1).atZone(ZoneId.systemDefault()).toInstant().toString();
                                    b.filter(f -> f.range(r -> r.field("createdTime").gte(JsonData.of(oneWeekAgoStr))));
                                    break;
                                case 4:
                                    String oneMonthAgoStr = now.minusMonths(1).atZone(ZoneId.systemDefault()).toInstant().toString();
                                    b.filter(f -> f.range(r -> r.field("createdTime").gte(JsonData.of(oneMonthAgoStr))));
                                    break;
                                default:
                                    throw new LeadNewsException("错误的时间排序");
                            }
                            return b;
                        })
                )
                // 指定要高亮的字段将其加上头尾标签
                .withHighlightQuery(
                        new HighlightQuery(
                                new Highlight(
                                        HighlightParameters.builder().withPreTags(pre).withPostTags(post).build(),
                                        List.of(new HighlightField("name"))
                                ), String.class)
                )
                .withPageable(PageRequest.of(page.intValue(), size.intValue()))
                //粉丝最多排序  默认
                // .withSort(Sort.sort(UserInfoDocument.class).by(UserInfoDocument::getFans).descending())
                .build();

        //排序控制
        if (sort == 1) {
            nativeQueryquery.addSort(Sort.sort(UserInfoDocument.class).by(UserInfoDocument::getCreatedTime).descending());
        } else if (sort == 0 || sort == 2) {
            nativeQueryquery.addSort(Sort.sort(UserInfoDocument.class).by(UserInfoDocument::getFans).descending());
        } else {
            throw new LeadNewsException("错误的排序");
        }

        SearchHits<UserInfoDocument> search = elasticsearchOperations.search(nativeQueryquery, UserInfoDocument.class);


        return search.stream().map(hit -> {
            // 打印原始文档内容
            UserInfoDocument content = hit.getContent();
            // 打印高亮字段
            hit.getHighlightFields().forEach((fieldName, highlight) -> {
                String highlightTitle = String.join(", ", highlight);
                content.setName(highlightTitle);
            });
            return content;
        }).toList();

    }

    private UserInfoVo userInfoIdOne(Long id){
        UserInfoVo redisUser= redisService.getCacheObject(redisUserInfoKey(id));
        if (null==redisUser) {
            ApUser apUser = apUserMapper.selectOneById(id);
            if (null == apUser) {
                throw new LeadNewsException("错误的用户");
            }
            UserInfoVo userInfoVo = mapper.selectGetInfo(id);
            userInfoVo.setPhone(maskPhoneNumber(userInfoVo.getPhone()));


            redisService.setCacheObject(redisUserInfoKey(id), userInfoVo);
            return userInfoVo;
        }

        return redisUser;
    }

    private ApUserInfo userVerification() {
        Long userid = RequestContextUtil.getUserId();
        ApUserInfo info = mapper.selectOneByUserId(userid);
        CustomSimpleThrowUtils.ObjectIsEmpty(info, "错误的用户");

        return info;
    }

    public static String maskPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.length() < 7) {
            return phoneNumber; // 如果手机号过短，直接返回
        }
        // 使用正则表达式替换手机号中间的部分为星号
        return phoneNumber.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    }
}
