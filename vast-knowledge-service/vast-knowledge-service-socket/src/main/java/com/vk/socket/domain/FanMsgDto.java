package com.vk.socket.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 粉丝消息
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FanMsgDto extends  BaseMsgDto{
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 关注或取消关注
     */
    private Integer followOfUnfollow;
}
