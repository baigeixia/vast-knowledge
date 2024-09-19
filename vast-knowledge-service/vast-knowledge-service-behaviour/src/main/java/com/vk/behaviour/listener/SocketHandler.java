package com.vk.behaviour.listener;

import com.alibaba.fastjson2.JSON;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import com.mybatisflex.core.query.QueryWrapper;
import com.vk.behaviour.common.utils.ws.SocketConstants;
import com.vk.behaviour.domain.ApCollectBehavior;
import com.vk.behaviour.domain.ApFollowBehavior;
import com.vk.behaviour.domain.ApLikesBehavior;
import com.vk.behaviour.domain.ApReadBehavior;
import com.vk.behaviour.domain.dto.ChatMsgDto;
import com.vk.behaviour.domain.dto.CollectMsgDto;
import com.vk.behaviour.domain.dto.CommentMsg;
import com.vk.behaviour.domain.dto.FanMsgDto;
import com.vk.behaviour.domain.table.ApCollectBehaviorTableDef;
import com.vk.behaviour.domain.vo.AckDataMsg;
import com.vk.behaviour.mapper.*;
import com.vk.behaviour.notifications.PushNotificationsHandler;
import com.vk.common.core.exception.LeadNewsException;
import com.vk.common.core.utils.StringUtils;
import com.vk.common.core.utils.TokenUtils;
import com.vk.common.core.utils.threads.TaskVirtualExecutorUtil;
import com.vk.common.mq.common.MqConstants;
import com.vk.common.mq.domain.NewCommentUpMsg;
import com.vk.common.mq.domain.NewUserMsg;
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
import java.util.Objects;
import java.util.UUID;

import static com.vk.behaviour.common.BeCommonConstants.BASE_LiKE;
import static com.vk.behaviour.common.BeCommonConstants.BASE_LiKE_NO;
import static com.vk.behaviour.domain.table.ApCollectBehaviorTableDef.AP_COLLECT_BEHAVIOR;
import static com.vk.behaviour.domain.table.ApFollowBehaviorTableDef.AP_FOLLOW_BEHAVIOR;
import static com.vk.behaviour.domain.table.ApLikesBehaviorTableDef.AP_LIKES_BEHAVIOR;
import static com.vk.behaviour.domain.table.ApReadBehaviorTableDef.AP_READ_BEHAVIOR;
import static com.vk.common.mq.common.MqConstants.SocketType.*;

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

    @Autowired
    private ApCollectBehaviorMapper apCollectBehaviorMapper;

    @Autowired
    private ApForwardBehaviorMapper apForwardBehaviorMapper;

    @Autowired
    private ApReadBehaviorMapper apReadBehaviorMapper;

    @Autowired
    private ApShowBehaviorMapper apShowBehaviorMapper;

    @Autowired
    private ApShareBehaviorMapper apShareBehaviorMapper;

    @Autowired
    private ApUnlikesBehaviorMapper apUnlikesBehaviorMapper;

    @Autowired
    private PushNotificationsHandler pushNotificationsHandler;



    /**
     * 当客户端发起连接时调用
     */
    @OnConnect
    public void onConnect(SocketIOClient socketIOClient) {
        TaskVirtualExecutorUtil.executeWith(() -> {
            UUID sessionId = socketIOClient.getSessionId();
            log.info("发起连接 : {}", sessionId);

            Long userId = clientGetUserId(socketIOClient);
            if (!StringUtils.isLongEmpty(userId)) {
                String userIdKey = SocketConstants.getRedisUserIdKey(userId);
                redisService.setCacheObject(userIdKey, sessionId);
            } else {
                log.error("错误的用户： {}", sessionId);
            }
        });
    }


    /**
     * 客户端断开连接时调用，刷新客户端信息
     */
    @OnDisconnect
    public void onDisConnect(SocketIOClient socketIOClient) {
        TaskVirtualExecutorUtil.executeWith(() -> {
            UUID sessionId = socketIOClient.getSessionId();
            log.info(sessionId + " 断开连接");

            Long userId = clientGetUserId(socketIOClient);
            if (!StringUtils.isLongEmpty(userId)) {
                String userIdKey = SocketConstants.getRedisUserIdKey(userId);
                redisService.deleteObject(userIdKey);
            } else {
                log.error("错误的用户： {}", sessionId);
            }
        });
    }


    /**
     * likeMsg 点赞后事件
     */
    @OnEvent("likeMsg")
    public void likeMsg(SocketIOClient socketIOClient, AckRequest ackRequest, ApLikesBehavior dto) {
        UUID sessionId = socketIOClient.getSessionId();
        log.info("LikeMsg 点赞 事件 -> dto:{} sessionId:{}", dto, sessionId);
        Long authorId = clientGetUserId(socketIOClient);
        String userName = clientGetUserName(socketIOClient);

        validaParameter(ackRequest,authorId,"未登录");
        validaParameter(ackRequest,userName,"未登录");

        Integer type = dto.getType();
        Long commentId = dto.getCommentId();
        Long articleId = dto.getArticleId();
        String repayAuthorName = dto.getAuthorName();
        Long repayAuthorId = dto.getRepayAuthorId();
        validaParameter(ackRequest, repayAuthorId, "被点赞人不能为空");
        validaParameter(ackRequest, articleId, "文章id不能为空");

        if (type == LIKE_TYPE_ARTICLE_NO) {
            validaParameter(ackRequest, commentId, "评论id不能为空");
        }

        //不能与自己互动
        excludeYourself(ackRequest,authorId,repayAuthorId);


        QueryWrapper where = QueryWrapper.create().where(AP_LIKES_BEHAVIOR.AUTHOR_ID.eq(authorId)
                .and(AP_LIKES_BEHAVIOR.COMMENT_ID.eq(commentId, type == LIKE_TYPE_ARTICLE_NO)
                        .and(AP_LIKES_BEHAVIOR.ARTICLE_ID.eq(articleId))));

        ApLikesBehavior behavior = apLikesBehaviorMapper.selectOneByQuery(where);

        ApLikesBehavior likesBehavior = new ApLikesBehavior();

        if (null == behavior) {
            BeanUtils.copyProperties(dto, likesBehavior);
            likesBehavior.setCreatedTime(LocalDateTime.now());
            likesBehavior.setOperation(BASE_LiKE);
            likesBehavior.setAuthorId(authorId);
            likesBehavior.setAuthorName(userName);
        } else {
            likesBehavior.setId(behavior.getId());
            likesBehavior.setOperation(behavior.getOperation() == BASE_LiKE ? BASE_LiKE_NO : BASE_LiKE);
        }
        apLikesBehaviorMapper.insertOrUpdate(likesBehavior, true);


        int num = likesBehavior.getOperation() == BASE_LiKE ? 1 : -1;
        if (StringUtils.isLongEmpty(commentId)) {
            int messageType = likesBehavior.getOperation() == BASE_LiKE ? LIKE : LIKE_NO;
            streamProcessingStandard(
                    socketIOClient, repayAuthorId, repayAuthorName, articleId, messageType,
                    UpdateArticleMess.UpdateArticleType.LIKES,
                    num
            );
        }else {
            int messageType = likesBehavior.getOperation() == BASE_LiKE ? LIKE_COMMENT : LIKE_COMMENT_NO;
            streamProcessingStandard(repayAuthorId, repayAuthorName,messageType, socketIOClient);
            streamProcessingStandard(commentId, num);
        }

    }


    /**
     * fanMsg 关注后事件
     */
    @OnEvent("fanMsg")
    public void fanMsg(SocketIOClient socketIOClient, AckRequest ackRequest, FanMsgDto dto) {
        UUID sessionId = socketIOClient.getSessionId();
        log.info("fanMsg 关注事件 -> dto:{} sessionId:{}", dto, sessionId);
        Long authorId = clientGetUserId(socketIOClient);
        String userName = clientGetUserName(socketIOClient);
        Long followId = dto.getFollowId();
        String followName = dto.getFollowName();
        validaParameter(ackRequest,authorId,"粉丝错误");
        validaParameter(ackRequest,followName,"粉丝名称不能为空");
        validaParameter(ackRequest,followId,"被关注人不能为空");

        excludeYourself(ackRequest,authorId,followId);

        ApFollowBehavior followBehavior = apFollowBehaviorMapper.selectOneByQuery(
                QueryWrapper.create()
                        .where(AP_FOLLOW_BEHAVIOR.AUTHOR_ID.eq(authorId))
                        .and(AP_FOLLOW_BEHAVIOR.FOLLOW_ID.eq(followId))
        );

        int messageType;
        if (null == followBehavior) {
            ApFollowBehavior behavior = new ApFollowBehavior();
            behavior.setAuthorId(authorId);
            behavior.setAuthorName(userName);
            behavior.setFollowId(followId);
            behavior.setCreatedTime(LocalDateTime.now());
            apFollowBehaviorMapper.insert(behavior);
            messageType=FOLLOW;
        } else {
            apFollowBehaviorMapper.deleteById(followBehavior.getId());
            messageType=FOLLOW_NO;
        }

        streamProcessingStandard(followId, followName,messageType, socketIOClient);

    }

    /**
     * commentMsg 评论后事件
     */
    @OnEvent("commentMsg")
    public void commentMsg(SocketIOClient socketIOClient, AckRequest ackRequest, CommentMsg dto) {
        log.info("commentMsg 评论 事件 {}", dto);
        Long authorId = clientGetUserId(socketIOClient);
        validaParameter(ackRequest,authorId,"未登录");


        Long articleId = dto.getArticleId();
        if (StringUtils.isLongEmpty(articleId)) {
            errorMessage(ackRequest, "文章id不能为空");
        }
        Long senderId = dto.getSenderId();
        if (StringUtils.isLongEmpty(senderId)) {
            errorMessage(ackRequest, "回复用户id不能为空");
        }
        String senderName = dto.getSenderName();
        if (StringUtils.isEmpty(senderName)) {
            errorMessage(ackRequest, "发送人名称不能为空");
        }

        excludeYourself(ackRequest,authorId,senderId);

        // 流标准 通知类型 与 kafka 流处理
        streamProcessingStandard(socketIOClient, senderId, senderName, articleId, COMMENT, UpdateArticleMess.UpdateArticleType.COMMENT, 1);
    }

    /**
     * chatMsg 私信消息发送事件
     */
    @OnEvent("chatMsg")
    public void chatMsg(SocketIOClient socketIOClient, AckRequest ackRequest, ChatMsgDto dto) {
        log.info("chatMsg 私信 事件 {}", dto);
        Long authorId = clientGetUserId(socketIOClient);
        validaParameter(ackRequest,authorId,"未登录");

        Long senderId = dto.getSenderId();
        validaParameter(ackRequest, senderId, "发送人id不能为空");
        String senderName = dto.getSenderName();
        validaParameter(ackRequest, senderName, "发送人名称不能为空");
        String content = dto.getContent();
        validaParameter(ackRequest, content.trim(), "内容不能为空");

        excludeYourself(ackRequest,authorId,senderId);

        // 流标准 通知类型处理 不包括kafka  提供 私信使用
        streamProcessingStandard(senderId, senderName, content, socketIOClient);
    }

    /**
     * collect 收藏事件
     */
    @OnEvent("collect")
    public void collect(SocketIOClient socketIOClient, AckRequest ackRequest, CollectMsgDto dto) {
        log.info("collect 收藏事件 {}", dto);
        Long authorId = clientGetUserId(socketIOClient);
        String userName = clientGetUserName(socketIOClient);
        validaParameter(ackRequest,authorId,"未登录");

        Long senderId = dto.getSenderId();
        if (StringUtils.isLongEmpty(senderId)) {
            errorMessage(ackRequest, "发送人id不能为空");
        }
        String senderName = dto.getSenderName();
        if (StringUtils.isEmpty(senderName)) {
            errorMessage(ackRequest, "发送人名称不能为空");
        }
        Long articleId = dto.getArticleId();
        if (StringUtils.isLongEmpty(senderId)) {
            errorMessage(ackRequest, "文章id不能为空");
        }

        ApCollectBehavior collectBehavior = apCollectBehaviorMapper.selectOneByQuery(QueryWrapper.create().where(AP_COLLECT_BEHAVIOR.AUTHOR_ID.eq(authorId).and(
                AP_COLLECT_BEHAVIOR.ARTICLE_ID.eq(articleId)
        )));
        ApCollectBehavior behavior = new ApCollectBehavior();
        if (null==collectBehavior){
            behavior.setArticleId(articleId);
            behavior.setOperation(0);
            behavior.setAuthorId(authorId);
            behavior.setCreatedTime(LocalDateTime.now());
            behavior.setAuthorName(userName);
        }else {
            behavior.setId(collectBehavior.getId());
            behavior.setOperation(1);
        }

        apCollectBehaviorMapper.insertOrUpdate(behavior);

        excludeYourself(ackRequest,authorId,senderId);
        streamProcessingStandard(socketIOClient, senderId, senderName, articleId, COLLECT, UpdateArticleMess.UpdateArticleType.COLLECTION, 1);

    }


    /**
     * read 阅读事件
     */
    @OnEvent("read")
    public void read(SocketIOClient socketIOClient, AckRequest ackRequest, ApReadBehavior dto) {
        UUID sessionId = socketIOClient.getSessionId();

        log.info("read 阅读 事件 {}", dto);
        Long articleId = dto.getArticleId();
        validaParameter(ackRequest, articleId, "文章id不能为空");
        Long userId = clientGetUserId(socketIOClient);
        validaParameter(ackRequest,userId,"未登录");


        ApReadBehavior readBehavior = apReadBehaviorMapper.selectOneByQuery(
                QueryWrapper.create().where(
                        AP_READ_BEHAVIOR.ENTRY_ID.eq(userId).and(AP_READ_BEHAVIOR.ARTICLE_ID.eq(articleId))
                )
        );

        if (null == readBehavior) {
            apReadBehaviorMapper.insert(dto);
            streamProcessingStandard(articleId, UpdateArticleMess.UpdateArticleType.VIEWS, 1);
        } else {
            apReadBehaviorMapper.update(dto, true);
        }
    }


    private void  excludeYourself(AckRequest ackRequest,Long senderId,Long localId){
        if (Objects.equals(senderId, localId)){
            errorMessage(ackRequest,"不能与自己互动",400);
        }
    }
    /**
     * 流标准 通知类型 与 kafka 流处理
     *
     * @param socketIOClient socketIO客户端
     * @param senderId       发送人id
     * @param senderName     发送人名称
     * @param articleId      文章id
     * @param type           发送类型
     * @param collection     流处理类型
     */
    private void streamProcessingStandard(
            SocketIOClient socketIOClient, Long senderId, String senderName, Long articleId, Integer type,
            UpdateArticleMess.UpdateArticleType collection, Integer num
    ) {
        streamProcessingStandard(senderId, senderName, type, socketIOClient);
        streamProcessingStandard(articleId, collection, num);
    }

    /**
     * 流标准 通知类型处理 不包括kafka
     *
     * @param senderId       发送人id
     * @param senderName     发送人名称
     * @param type           发送类型
     * @param socketIOClient socketIO客户端
     */
    private void streamProcessingStandard(Long senderId, String senderName, Integer type, SocketIOClient socketIOClient) {
        conventionalNewUserMsg(senderId, senderName, null, type, socketIOClient);
    }

    /**
     * 流标准 通知类型处理 不包括kafka  提供 私信使用
     *
     * @param senderId       发送人id
     * @param senderName     发送人名称
     * @param socketIOClient socketIO客户端
     */
    private void streamProcessingStandard(Long senderId, String senderName, String content, SocketIOClient socketIOClient) {
        conventionalNewUserMsg(senderId, senderName, content, CHAT_MSG, socketIOClient);
    }

    private void conventionalNewUserMsg(Long senderId, String senderName, String content, Integer type, SocketIOClient socketIOClient) {
        Long authorId = clientGetUserId(socketIOClient);
        String userName = clientGetUserName(socketIOClient);
        if (StringUtils.isLongEmpty(authorId)){
            throw  new LeadNewsException("用户错误");
        }

        if (StringUtils.isEmpty(userName)){
            throw  new LeadNewsException("用户错误");
        }

        NewUserMsg message = new NewUserMsg();
        message.setUserId(authorId);
        message.setUserName(userName);
        message.setSenderId(senderId);
        message.setContent(content);
        message.setSenderName(senderName);
        message.setType(type);
        // 消息通知存储
        kafkaTemplate.send(MqConstants.TopicCS.NEWS_USER_MESSAGE_TOPIC, JSON.toJSONString(message));
        // 同步通知用户
        pushNotificationsHandler.msgPush(message);
    }

    /**
     * 流标准 单独kafka流处理 只做添加
     *
     * @param articleId  文章id
     * @param collection 流处理类型
     */
    private void streamProcessingStandard(Long articleId, UpdateArticleMess.UpdateArticleType collection, Integer num) {
        // 流处理消息 更新文章数据
        UpdateArticleMess msg = new UpdateArticleMess();
        msg.setArticleId(articleId);
        msg.setNum(num);
        msg.setType(collection);
        kafkaTemplate.send(MqConstants.TopicCS.HOT_ARTICLE_SCORE_TOPIC, JSON.toJSONString(msg));
    }

    /**
     * 流标准 单独kafka流处理 只做添加  评论添加
     *
     * @param commentId  评论id
     * @param num 添加减少
     */
    private void streamProcessingStandard(Long commentId, Integer num) {
        // 流处理消息 更新文章数据
        NewCommentUpMsg msg = new NewCommentUpMsg();
        msg.setCommentId(commentId);
        msg.setNum(num);
        kafkaTemplate.send(MqConstants.TopicCS.NEWS_COMMENT_TOPIC, JSON.toJSONString(msg));
    }


    private void validaParameter(AckRequest ackRequest, Long par, String msg) {
        if (StringUtils.isLongEmpty(par)) {
            errorMessage(ackRequest,msg);
        }
    }

    private void validaParameter(AckRequest ackRequest, String par, String msg) {
        if (StringUtils.isEmpty(par)) {
            errorMessage(ackRequest, msg);
        }
    }



    private void errorMessage(AckRequest ackRequest, String msg,Integer code) {
        errorMessage(ackRequest,AckDataMsg.setAckDataMsg(msg,code));
    }

    private void errorMessage(AckRequest ackRequest, String msg) {
        errorMessage(ackRequest,AckDataMsg.setAckDataMsg(msg));
    }

    private void errorMessage(AckRequest ackRequest, AckDataMsg msg) {
        ackRequest.sendAckData(msg);
        throw new LeadNewsException(msg.getMsg());
    }


    private Long clientGetUserId(SocketIOClient socketIOClient) {
        UUID sessionId = socketIOClient.getSessionId();
        String token = clientGetToken(socketIOClient, sessionId);
        if (StringUtils.isNotEmpty(token)) {
            String userId = TokenUtils.getUserId(token);
            if (StringUtils.isNotEmpty(userId)) {
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
