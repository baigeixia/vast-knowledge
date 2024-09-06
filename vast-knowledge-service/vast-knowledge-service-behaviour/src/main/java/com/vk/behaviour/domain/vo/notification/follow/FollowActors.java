package com.vk.behaviour.domain.vo.notification.follow;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FollowActors {

    /**
     * 用户Id
     */
    private  Long id;

    /**
     * 用户姓名
     */
    private  String username;

}
