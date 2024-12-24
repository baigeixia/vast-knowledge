package com.vk.comment.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Actors {

    /**
     * 用户Id
     */
    private  Long id;

    /**
     * 提示消息
     */
    private  String verb;

    /**
     * 用户姓名
     */
    private  String username;
    /**
     * 回复内容
     */
    private  String replyContent;
    /**
     * 回复图片
     */
    private String image;
    /**
     * 回复内容 时间
     */
    private String replyContentTime;
}
