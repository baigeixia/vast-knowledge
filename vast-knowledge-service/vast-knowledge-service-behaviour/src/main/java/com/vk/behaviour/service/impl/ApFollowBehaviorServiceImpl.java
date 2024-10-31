package com.vk.behaviour.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.vk.behaviour.domain.ApFollowBehavior;
import com.vk.behaviour.domain.entity.FollowBehaviorTimeCount;
import com.vk.behaviour.domain.entity.FollowNotifyCountMap;
import com.vk.behaviour.domain.vo.notification.follow.FollowActors;
import com.vk.behaviour.domain.vo.notification.follow.FollowNotificationInfo;
import com.vk.behaviour.domain.vo.notification.follow.FollowNotificationListVo;
import com.vk.behaviour.mapper.ApFollowBehaviorMapper;
import com.vk.behaviour.service.ApFollowBehaviorService;
import com.vk.common.core.utils.RequestContextUtil;
import com.vk.common.core.utils.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * APP关注行为 服务层实现。
 *
 * @author 张三
 * @since 2024-05-13
 */
@Service
public class ApFollowBehaviorServiceImpl extends ServiceImpl<ApFollowBehaviorMapper, ApFollowBehavior> implements ApFollowBehaviorService {

    @Override
    public List<FollowNotificationListVo> followList(Long userId, Long page, Long size) {
        List<FollowNotificationListVo> vos = new ArrayList<>();
        if (StringUtils.isLongEmpty(userId)){
             userId = RequestContextUtil.getUserId();
        }

        page = (page - 1) * size;

        List<FollowNotifyCountMap> countMapList = mapper.getFollowNotifyCountMap(userId, page, size);
        Map<String, Integer> countMap = countMapList.stream()
                .collect(Collectors.toMap(
                        FollowNotifyCountMap::getDateTime,
                        FollowNotifyCountMap::getTotal
                ));

        if (!ObjectUtils.isEmpty(countMap)) {
            List<FollowBehaviorTimeCount> followList = mapper.getFollowNotifyList(userId, page, size);
            vos = transformToFollowNotificationListVo(followList, countMap);
        }
        return vos;
    }

    private List<FollowNotificationListVo> transformToFollowNotificationListVo(List<FollowBehaviorTimeCount> followList, Map<String, Integer> countMap) {
        Map<String, List<FollowBehaviorTimeCount>> groupedByDateTime = followList.stream()
                .collect(Collectors.groupingBy(FollowBehaviorTimeCount::getDateTime));

        return groupedByDateTime.entrySet().stream()
                .sorted(Map.Entry.<String, List<FollowBehaviorTimeCount>>comparingByKey().reversed()) // 按键倒序排列
                .map(entry -> {
                    String dateTime = entry.getKey();
                    List<FollowBehaviorTimeCount> counts = entry.getValue();

                    List<FollowActors> followActors = counts.stream().map(i ->
                            new FollowActors(
                                    i.getArticleId(),
                                    i.getAuthorName()
                            )
                    ).toList();

                    FollowNotificationInfo notificationInfo = new FollowNotificationInfo(
                            "关注了您",
                            countMap.get(dateTime),
                            // counts.getFirst().getFollowEndTime().toLocalTime().toString(),
                            counts.get(0).getFollowEndTime().toLocalTime().toString(),
                            followActors
                    );
                    return new FollowNotificationListVo(dateTime, Collections.singletonList(notificationInfo));
                })
                .collect(Collectors.toList());
    }
}
