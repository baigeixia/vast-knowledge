package com.vk.behaviour.domain.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ReadDataAnalysisVo {
    /**
     * 总阅读量
     */
    private  Long readTotal;
    /**
     * 平均阅读进度
     */
    private  Integer averageRead;
    /**
     * 平均阅读进度 时间（秒）
     */
    private  Integer averageReadTime;
    /**
     * 跳出
     */
    private  Integer bounceRate;
    /**
     * 收藏
     */
    private  Long collect;
    /**
     * 点赞
     */
    private  Long like;
    /**
     * 不喜欢
     */
    private  Long dislike;
    /**
     * 转发
     */
    private  Long forward;
    /**
     * 评论
     */
    private  Long comment;
    /**
     * 阅读完成度
     */
    private List<ReadCompletionAnalysis> readAnalysis=new ArrayList<>();
}
