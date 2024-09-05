package com.vk.behaviour.domain.vo;


import lombok.Data;

@Data
public class AttachInfo {
    /**
     * 文章id
     */
    private  Long id;
    /**
     * 评论id
     */
    private  Long commentId;
    /**
     * 文章标题
     */
    private  String title;


}
