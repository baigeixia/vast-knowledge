package com.vk.behaviour.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.vk.behaviour.domain.ApLikesBehavior;
import com.vk.behaviour.domain.entity.LikesBehaviorTimeCount;
import com.vk.behaviour.domain.vo.Actors;
import com.vk.behaviour.domain.vo.AttachInfo;
import com.vk.behaviour.domain.vo.LikeNotificationListVo;
import com.vk.behaviour.domain.vo.NotificationInfo;
import com.vk.behaviour.mapper.ApLikesBehaviorMapper;
import com.vk.behaviour.service.ApLikesBehaviorService;
import com.vk.common.core.utils.RequestContextUtil;
import com.vk.common.core.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * APP点赞行为 服务层实现。
 *
 * @author 张三
 * @since 2024-05-13
 */
@Service
@Slf4j
public class ApLikesBehaviorServiceImpl extends ServiceImpl<ApLikesBehaviorMapper, ApLikesBehavior> implements ApLikesBehaviorService {


    @Override
    public List<LikeNotificationListVo> likeList(Long page, Long size) {
        List<LikeNotificationListVo> vos = new ArrayList<>();
        Long userId = RequestContextUtil.getUserId();
        // List<String> dataTime= mapper.getTimeRange((page-1)*size,size);
        List<LikesBehaviorTimeCount> rawData = mapper.getLikesBehaviorTimeCountList(userId, (page - 1) * size, size);
        if (!ObjectUtils.isEmpty(rawData)){
            List<LikeNotificationListVo> liked = rawData.stream()
                    .collect(Collectors.groupingBy(
                            LikesBehaviorTimeCount::getTime,
                            Collectors.mapping(
                                    data -> {
                                        NotificationInfo info = new NotificationInfo();
                                        info.setVerb("Liked");
                                        info.setMergeCount(data.getTotal());
                                        info.setCommentEndTime(data.getTime());
                                        // Placeholder for Actors and AttachInfo
                                        info.setActors(new Actors());
                                        info.setAttachInfo(new AttachInfo());
                                        return info;
                                    },
                                    Collectors.toList()
                            )
                    ))
                    .entrySet().stream()
                    .map(entry -> {
                        LikeNotificationListVo listVo = new LikeNotificationListVo();
                        listVo.setStatisticsTime(entry.getKey());
                        listVo.setNotificationInfoList(entry.getValue());
                        return listVo;
                    })
                    .toList();

            System.out.println(liked);

            // Print result (for demonstration)
            // result.forEach((time, vo) -> {
            //     System.out.println("Time: " + time);
            //     vo.getNotificationInfoList().forEach(info -> {
            //         System.out.println("Verb: " + info.getVerb());
            //         System.out.println("MergeCount: " + info.getMergeCount());
            //         System.out.println("CommentEndTime: " + info.getCommentEndTime());
            //         // Additional info
            //         System.out.println("Actors: " + info.getActors());
            //         System.out.println("AttachInfo: " + info.getAttachInfo());
            //     });
            // });
        }

        return vos;
    }
}
