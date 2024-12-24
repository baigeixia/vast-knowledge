package com.vk.comment.domain.vo;

import lombok.Data;

import java.util.List;

@Data
public class NotificationInfo {

    /**
     * 文章id
     */
    private  Long id;
    /**
     * 文章标题
     */
    private  String title;
    /**
     * 评论id
     */
    private  Long commentId;
    /**
     * 是否删除
     */
    private  Boolean hide;
    /**
     * 对应用户
     */
    private Actors actors;
}
