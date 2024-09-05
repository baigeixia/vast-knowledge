package com.vk.behaviour.domain.vo;

import lombok.Data;

@Data
public class Actors {

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
    private String replyContentTime;
}
