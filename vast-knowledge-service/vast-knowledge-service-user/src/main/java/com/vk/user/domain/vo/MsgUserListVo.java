package com.vk.user.domain.vo;

import com.mysql.cj.jdbc.jmx.LoadBalanceConnectionGroupManager;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MsgUserListVo {
    /**
     * 用户id 与名称
     */
    private Long id;
    private String name;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 最后一条消息
     */
    private String snippet;
    /**
     * 是否阅读 true 未阅读
     */
    private Integer unread;
    private LocalDateTime senderTime;
}
