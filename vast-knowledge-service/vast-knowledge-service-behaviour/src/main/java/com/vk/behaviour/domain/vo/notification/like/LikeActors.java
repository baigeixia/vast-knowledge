package com.vk.behaviour.domain.vo.notification.like;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LikeActors {

    /**
     * 用户Id
     */
    private  Long id;

    /**
     * 用户姓名
     */
    private  String username;

    /**
     * 回复内容 时间
     */
    private LocalDateTime replyContentTime;
}
