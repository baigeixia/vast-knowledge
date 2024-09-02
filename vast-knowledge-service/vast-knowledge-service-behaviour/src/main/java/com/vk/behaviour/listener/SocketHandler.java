package com.vk.behaviour.listener;

import com.alibaba.fastjson2.JSON;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import com.mybatisflex.core.query.QueryWrapper;
import com.vk.behaviour.common.BeCommonConstants;
import com.vk.behaviour.domain.ApFollowBehavior;
import com.vk.behaviour.domain.ApLikesBehavior;
import com.vk.behaviour.domain.ApReadBehavior;
import com.vk.behaviour.domain.ApShowBehavior;
import com.vk.behaviour.domain.dto.ChatMsgDto;
import com.vk.behaviour.domain.dto.CommentMsg;
import com.vk.behaviour.mapper.ApFollowBehaviorMapper;
import com.vk.behaviour.mapper.ApLikesBehaviorMapper;
import com.vk.common.core.exception.LeadNewsException;
import com.vk.common.core.utils.StringUtils;
import com.vk.common.core.utils.TokenUtils;
import com.vk.common.mq.common.MqConstants;
import com.vk.common.mq.domain.NewMsg;
import com.vk.common.mq.domain.UpdateArticleMess;
import com.vk.common.redis.service.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import static com.vk.behaviour.common.BeCommonConstants.BASE_LiKE;
import static com.vk.behaviour.common.BeCommonConstants.BASE_LiKE_NO;
import static com.vk.behaviour.domain.table.ApLikesBehaviorTableDef.AP_LIKES_BEHAVIOR;
import static com.vk.common.mq.common.MqConstants.SocketType.*;
import static com.vk.common.mq.common.MqConstants.TopicCS.NEWS_LIKE_TOPIC;
import static com.vk.common.redis.constants.BusinessConstants.SOCKET_USER_ID;

/**
 * SocketHandler
 *
 * @author wliduo[i@dolyw.com]
 * @date 2019/4/17 13:42
 */
@Component
public class SocketHandler {
    private final Logger log = LoggerFactory.getLogger(SocketHandler.class);

    @Autowired
    private RedisService redisService;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ApLikesBehaviorMapper apLikesBehaviorMapper;

    @Autowired
    private ApFollowBehaviorMapper apFollowBehaviorMapper;


    /**
     * socketIOServer
     */
    private final SocketIOServer socketIOServer;

    @Autowired
    public SocketHandler(SocketIOServer socketIOServer) {
        this.socketIOServer = socketIOServer;
    }

    /**
     * 当客户端发起连接时调用
     */
    @OnConnect
    public void onConnect(SocketIOClient socketIOClient) {
        UUID sessionId = socketIOClient.getSessionId();
        log.info("发起连接 : {}", sessionId);

        Long userId = clientGetUserId(socketIOClient);
        if (!StringUtils.isLongEmpty(userId)) {
            redisService.setCacheObject(getRedisUserIdKey(userId), sessionId);
        } else {
            log.error("错误的用户： {}", sessionId);
        }
    }

    private static String getRedisUserIdKey(Long userId) {
        return SOCKET_USER_ID + userId;
    }


    /**
     * 客户端断开连接时调用，刷新客户端信息
     */
    @OnDisconnect
    public void onDisConnect(SocketIOClient socketIOClient) {
        UUID sessionId = socketIOClient.getSessionId();
        log.info(sessionId + " 断开连接");

        Long userId = clientGetUserId(socketIOClient);
        if (StringUtils.isLongEmpty(userId)) {
            redisService.deleteObject(getRedisUserIdKey(userId));
        } else {
            log.error("错误的用户： {}", sessionId);
        }

    }


    /**
     * likeMsg 点赞后事件
     */
    @OnEvent("likeMsg")
    public void likeMsg(SocketIOClient socketIOClient, AckRequest ackRequest, ApLikesBehavior dto) {
        UUID sessionId = socketIOClient.getSessionId();
        log.info("LikeMsg 点赞 事件 -> dto:{} sessionId:{}", dto, sessionId);
        // Long authorId = dto.getAuthorId();
        Long authorId = clientGetUserId(socketIOClient);
        String userName = clientGetUserName(socketIOClient);

        Integer type = dto.getType();
        Long commentId = dto.getCommentId();
        Long articleId = dto.getArticleId();
        Integer operation = dto.getOperation();
        Long repayAuthorId = dto.getRepayAuthorId();

        if (StringUtils.isLongEmpty(repayAuthorId)) {
            log.error("被点赞人不能为空");
            return;
        }
        if (StringUtils.isLongEmpty(articleId)) {
            log.error("文章id不能为空");
            return;
        }

        if (type == LIKE_TYPE_ARTICLE) {
            if (StringUtils.isLongEmpty(articleId)) {
                log.error("文章id不能为空");
                return;
            }
        } else if (type == LIKE_TYPE_ARTICLE_NO) {
            if (StringUtils.isLongEmpty(commentId)) {
                log.error("评论id不能为空");
                return;
            }
        }else {
            return;
        }

        QueryWrapper where = QueryWrapper.create().where(AP_LIKES_BEHAVIOR.AUTHOR_ID.eq(authorId)
                .and(AP_LIKES_BEHAVIOR.COMMENT_ID.eq(commentId, type == LIKE_TYPE_ARTICLE_NO)
                .and(AP_LIKES_BEHAVIOR.ARTICLE_ID.eq(articleId))));
        ApLikesBehavior behavior = apLikesBehaviorMapper.selectOneByQuery(where);

        ApLikesBehavior likesBehavior = new ApLikesBehavior();
        if (null==behavior){
            BeanUtils.copyProperties(dto,likesBehavior);
            likesBehavior.setCreatedTime(LocalDateTime.now());
            likesBehavior.setOperation(BASE_LiKE);
            likesBehavior.setAuthorId(authorId);
            likesBehavior.setAuthorName(userName);
        }else {
            likesBehavior.setId(behavior.getId());
            likesBehavior.setOperation(behavior.getOperation() == BASE_LiKE ?BASE_LiKE_NO : BASE_LiKE);
        }
        apLikesBehaviorMapper.insertOrUpdate(likesBehavior,true);
        // if (operation ==  BASE_LiKE){
        //     if (null==behavior){
        //         ApLikesBehavior likesBehavior = new ApLikesBehavior();
        //         likesBehavior.setCommentId(commentId);
        //         likesBehavior.setCreatedTime(LocalDateTime.now());
        //         likesBehavior.setOperation(BASE_LiKE);
        //         likesBehavior.setType(type);
        //         likesBehavior.setArticleId(articleId);
        //         likesBehavior.setAuthorId(authorId);
        //         likesBehavior.setAuthorName(userName);
        //
        //         apLikesBehaviorMapper.insert(likesBehavior);
        //     }else {
        //         if (behavior.getOperation()==BASE_LiKE_NO) {
        //             ApLikesBehavior likesBehavior = new ApLikesBehavior();
        //             likesBehavior.setId(behavior.getId());
        //             likesBehavior.setOperation(BASE_LiKE);
        //             apLikesBehaviorMapper.update(likesBehavior);
        //         }
        //     }
        // }else {
        //     if(null == behavior){
        //         log.error("还没有点赞");
        //
        //         return;
        //     }
        //
        //     if (behavior.getOperation()==BASE_LiKE) {
        //         ApLikesBehavior likesBehavior = new ApLikesBehavior();
        //         likesBehavior.setId(behavior.getId());
        //         likesBehavior.setOperation(BASE_LiKE_NO);
        //         apLikesBehaviorMapper.update(likesBehavior);
        //     }
        // }

        UpdateArticleMess msg = new UpdateArticleMess();
        msg.setArticleId(articleId);
        msg.setType(UpdateArticleMess.UpdateArticleType.LIKES);
        msg.setNum(operation == BASE_LiKE  ? 1: -1);
log.info(JSON.toJSONString(msg));
        kafkaTemplate.send(MqConstants.TopicCS.HOT_ARTICLE_SCORE_TOPIC, JSON.toJSONString(msg));
        //点赞查询 点赞还是取消  true = 取消 false = 点赞
        // boolean isOperation = null == behavior || behavior.getOperation();
        //
        // dto.setId(!isOperation ? behavior.getId() : null);
        // if (operation == BASE_LiKE){
        //     dto.setAuthorId(authorId);
        //     dto.setAuthorName(userName);
        //     dto.setCreatedTime(LocalDateTime.now());
        // }else {
        //     dto.setArticleId(null);
        //     dto.setRepayAuthorId(null);
        //     dto.setType(null);
        //     dto.setCommentId(null);
        // }
        // dto.setOperation(!isOperation);
        // apLikesBehaviorMapper.insertOrUpdate(dto,true);

        // NewMsg newMsg = new NewMsg();
        // newMsg.setUserId(authorId);
        // newMsg.setSenderName(userName);
        // newMsg.setSenderId(repayAuthorId);
        // newMsg.setCreatedTime(dto.getCreatedTime());

        /* 文章点赞
        if (type == LIKE_TYPE_ARTICLE) {
            newMsg.setType(isOperation ? LIKE_NO : LIKE);
            kafkaTemplate.send(NEWS_LIKE_TOPIC, JSON.toJSONString(newMsg));
        } else {
            newMsg.setType(isOperation ? LIKE_COMMENT_NO : LIKE_COMMENT);
            kafkaTemplate.send(NEWS_LIKE_TOPIC, JSON.toJSONString(newMsg));
        }*/

        // 发送消息给 kafka stream 的input topic，做实时计算


        // String repaySessionId = redisService.getCacheObject(getRedisUserIdKey(repayAuthorId));
        // SocketIOClient client = socketIOServer.getClient(UUID.fromString(repaySessionId));
        // client.sendEvent(MqConstants.UserSocketCS.NEWS_LIKE, "test new_like");
    }

    /**
     * commentMsg 评论后事件
     */
    @OnEvent("commentMsg")
    public void commentMsg(SocketIOClient socketIOClient, AckRequest ackRequest, CommentMsg dto) {
        log.info("commentMsg 评论 事件 {}", dto);
    }

    /**
     * fanMsg 关注后事件
     */
    @OnEvent("fanMsg")
    public void fanMsg(SocketIOClient socketIOClient, AckRequest ackRequest, ApFollowBehavior dto) {
        UUID sessionId = socketIOClient.getSessionId();
        log.info("fanMsg 关注事件 -> dto:{} sessionId:{}", dto, sessionId);
        Long authorId = dto.getAuthorId();
        if (StringUtils.isLongEmpty(authorId)) {
            log.error("用户错误");
        }
        Long followId = dto.getFollowId();
        if (StringUtils.isLongEmpty(followId)) {
            log.error("被关注人不能为空");
        }
        apFollowBehaviorMapper.insert(dto);
    }

    /**
     * chatMsg 私信消息发送事件
     */
    @OnEvent("chatMsg")
    public void chatMsg(SocketIOClient socketIOClient, AckRequest ackRequest, ChatMsgDto dto) {
        log.info("chatMsg 私信 事件 {}", dto);
    }

    /**
     * display 展示消息发送事件
     */
    @OnEvent("display")
    public void display(SocketIOClient socketIOClient, AckRequest ackRequest, ApShowBehavior dto) {
        UUID sessionId = socketIOClient.getSessionId();
        log.info("display 展示 事件 {}", dto);
    }

    /**
     * read 阅读消息发送事件
     */
    @OnEvent("read")
    public void read(SocketIOClient socketIOClient, AckRequest ackRequest, ApReadBehavior dto) {
        UUID sessionId = socketIOClient.getSessionId();
        log.info("display 展示 事件 {}", dto);
    }

    private Long clientGetUserId(SocketIOClient socketIOClient) {
        UUID sessionId = socketIOClient.getSessionId();
        String token = clientGetToken(socketIOClient, sessionId);
        if (StringUtils.isNotEmpty(token)) {
            String userId = TokenUtils.getUserId(token);
            if (StringUtils.isNotEmpty(userId)){
                return Long.valueOf(userId);
            }
            return null;
        }
        return null;
    }

    private String clientGetUserName(SocketIOClient socketIOClient) {
        UUID sessionId = socketIOClient.getSessionId();
        String token = clientGetToken(socketIOClient, sessionId);
        if (StringUtils.isNotEmpty(token)) {
            return TokenUtils.getUserName(token);
        }
        return null;
    }

    private String clientGetToken(SocketIOClient socketIOClient, UUID sessionId) {
        Object authToken = socketIOClient.getHandshakeData().getAuthToken();
        log.info(sessionId + "断开连接");
        if (null != authToken) {
            if (authToken instanceof LinkedHashMap) {
                @SuppressWarnings("unchecked")
                Map<String, Object> authTokenMap = (LinkedHashMap<String, Object>) authToken;
                return (String) authTokenMap.get("token");
            }
        } else {
            log.error("未登录 ： {}", sessionId);
            // throw new LeadNewsException(401,"未登录");
        }
        return null;
    }


}
