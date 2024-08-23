package com.vk.socket.listener;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import com.vk.socket.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * SocketHandler
 *
 * @author wliduo[i@dolyw.com]
 * @date 2019/4/17 13:42
 */
@Component
public class SocketHandler {
    private final Logger log = LoggerFactory.getLogger(SocketHandler.class);

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
    public void onConnect( SocketIOClient socketIOClient) {
        UUID sessionId = socketIOClient.getSessionId();
        log.info(sessionId +"发起连接");
    }

    /**
     * 客户端断开连接时调用，刷新客户端信息
     */
    @OnDisconnect
    public void onDisConnect(SocketIOClient socketIOClient) {
        UUID sessionId = socketIOClient.getSessionId();
        log.info(sessionId +" 断开连接");
    }

    /**
     * likeMsg 点赞后事件
     */
    @OnEvent("likeMsg")
    public void likeMsg(SocketIOClient socketIOClient, AckRequest ackRequest, LikeMsgDto dto) {
        UUID sessionId = socketIOClient.getSessionId();
        log.info("LikeMsg 点赞 事件 -> dto:{} sessionId:{}",dto,sessionId);
    }
    /**
     * commentMsg 评论后事件
     */
    @OnEvent("commentMsg")
    public void commentMsg(SocketIOClient socketIOClient, AckRequest ackRequest, CommentMsg dto) {
        log.info("commentMsg 评论 事件 {}",dto);
    }
    /**
     * fanMsg 关注后事件
     */
    @OnEvent("fanMsg")
    public void fanMsg(SocketIOClient socketIOClient, AckRequest ackRequest, FanMsgDto dto) {
        log.info("fanMsg 关注事件 {}",dto);
    }
    /**
     * chatMsg 私信消息发送事件
     */
    @OnEvent("chatMsg")
    public void chatMsg(SocketIOClient socketIOClient, AckRequest ackRequest, ChatMsgDto dto) {
        log.info("chatMsg 私信 事件 {}",dto);
    }


}
