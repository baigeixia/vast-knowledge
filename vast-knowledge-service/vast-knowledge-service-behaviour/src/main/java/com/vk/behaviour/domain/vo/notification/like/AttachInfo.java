package com.vk.behaviour.domain.vo.notification.like;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
