package com.vk.behaviour.notifications;


import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.vk.behaviour.common.utils.ws.SocketConstants;
import com.vk.common.core.utils.StringUtils;
import com.vk.common.core.utils.threads.TaskVirtualExecutorUtil;
import com.vk.common.mq.domain.NewUserMsg;
import com.vk.common.redis.service.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.UUID;

import static com.vk.common.mq.common.MqConstants.SocketType.*;

import static com.vk.common.mq.common.MqConstants.UserSocketCS.*;


@Component
public class PushNotificationsHandler {

    private final Logger log = LoggerFactory.getLogger(PushNotificationsHandler.class);

    @Autowired
    private SocketIOServer socketIOServer;

    @Autowired
    private RedisService redisService;

    public void msgPush(NewUserMsg msg) {
        UUID uuid = UUID.randomUUID();
        Long sendId = msg.getSenderId();
        Integer type = msg.getType();

        if (StringUtils.isLongEmpty(sendId)){
            log.error("uuid ： {} => 发送消息 sendId 为空 type： {}",uuid ,type);
            return;
        }

        if (ObjectUtils.isEmpty(type)){
            log.error("uuid ： {} =>  发送消息 sendId ：{} type 为空",uuid ,sendId);
            return;
        }

        String clientId = getClientId(sendId, uuid, type);
        if (clientId == null) return;

        SocketIOClient client = socketIOServer.getClient(UUID.fromString(clientId));

        if (null==client){
            log.info("uuid ： {} =>  client 为空 连接失败",uuid);
            return;
        }

        TaskVirtualExecutorUtil.executeWith(() -> {
            log.info("uuid ： {} => 向 sendId ：{} 发送消息 type： {}",uuid, sendId, type);

            switch (type){
                case FOLLOW:
                    TaskVirtualExecutorUtil.executeWith(() -> {
                        log.info("发送消息 FOLLOW 关注 <= uuid ： {} ",uuid);
                        client.sendEvent(FAN_NOTIFICATION,msg);
                    });
                    break;
                case COLLECT:
                    TaskVirtualExecutorUtil.executeWith(() -> {
                        log.info("发送消息 COLLECT 收藏文章 <= uuid ： {}",uuid);
                        client.sendEvent(COLLECT_NOTIFICATION,msg);
                    });
                    break;
                case LIKE:
                    TaskVirtualExecutorUtil.executeWith(() -> {
                        log.info("发送消息 LIKE 点赞文章 <= uuid ： {}",uuid);
                        client.sendEvent(LIKE_NOTIFICATION,msg);
                    });
                    break;
                case SYSTEM:
                    TaskVirtualExecutorUtil.executeWith(() -> {
                        log.info("发送消息 SYSTEM 系统通知 <= uuid ： {}",uuid);
                        client.sendEvent(SYSTEM_MSG_NOTIFICATION,msg);
                    });
                    break;
                // case FORWARD:
                //     TaskVirtualExecutorUtil.executeWith(() -> {
                //         log.info("发送消息 FORWARD 转发文章  <= uuid ： {}",uuid);
                //         client.sendEvent(LIKE_NOTIFICATION,msg);
                //     });
                //     break;
                case LIKE_COMMENT:
                    TaskVirtualExecutorUtil.executeWith(() -> {
                        log.info("发送消息 LIKE_COMMENT 点赞评论 <= uuid ： {}",uuid);
                        client.sendEvent(LIKE_NOTIFICATION,msg);
                    });
                    break;
                // case LIKE_COMMENT_NO:
                //     TaskVirtualExecutorUtil.executeWith(() -> {
                //         log.info("发送消息 LIKE_COMMENT_NO 取消评论点赞 <= uuid ： {}",uuid);
                //
                //     });
                //     break;
                case CHAT_MSG:
                    TaskVirtualExecutorUtil.executeWith(() -> {
                        log.info("发送消息 CHAT_MSG 私信通知 <= uuid ： {}",uuid);
                        client.sendEvent(CHAT_MSG_NOTIFICATION,msg);
                    });
                    break;
                case COMMENT:
                    TaskVirtualExecutorUtil.executeWith(() -> {
                        log.info("发送消息 COLLECT 评论通知 <= uuid ： {}",uuid);
                        client.sendEvent(COMMENT_NOTIFICATION,msg);
                    });
                    break;
                // case SHARE:
                //     TaskVirtualExecutorUtil.executeWith(() -> {
                //         log.info("发送消息 COLLECT 分享通知 <= uuid ： {}",uuid);
                //     });
                //     break;
                default:
                    log.info("uuid ： {} => 不发送此类通知 ",uuid);
            }
            log.info("uuid ： {} =>发送消息 结束 sendId ：{}  type： {}",uuid, sendId, type);
        });

    }

    private String getClientId(Long sendId, UUID uuid, Integer type) {
        String userIdKey = SocketConstants.getRedisUserIdKey(sendId);
        String clientId = redisService.getCacheObject(userIdKey);
        if (StringUtils.isEmpty(clientId)){
            log.error("uuid ： {} => 发送消息 clientId 为空 type： {}", uuid, type);
            return null;
        }
        return clientId;
    }


}
