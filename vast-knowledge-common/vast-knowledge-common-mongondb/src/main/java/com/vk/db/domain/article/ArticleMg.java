package com.vk.db.domain.article;



import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

import java.time.LocalDateTime;

/**
 * 已发布的文章信息 实体类。
 *
 * @author 张三
 * @since 2024-07-11
 */
@Document
public class ArticleMg implements Serializable {


    @Id
    private Long id;

    /**
     * 标题
     */
    @Indexed
    private String title;

    /**
     * 文章作者的ID
     */
    private Long authorId;

    /**
     * 作者昵称
     */
    @Indexed
    private String authorName;

    /**
     * 文章所属频道ID
     */
    @Indexed
    private Long channelId;

    /**
     * 频道名称
     */
    @Indexed
    private String channelName;

    /**
     * 文章布局 0:无图文章 1:单图文章 2:多图文章
     */
    private Integer layout;

    /**
     * 文章标记 0:普通文章 1:热点文章 2:置顶文章 3:精品文章 4:大V文章
     */
    private Integer flag;

    /**
     * 文章图片,多张逗号分隔
     */
    private String images;

    /**
     * 文章标签最多3个,逗号分隔
     */
    @Indexed
    private String labels;

    /**
     * 点赞数量
     */
    @Indexed
    private Long likes;

    /**
     * 收藏数量
     */
    private Long collection;

    /**
     * 评论数量
     */
    @Indexed
    private Long comment;

    /**
     * 阅读数量
     */
    private Long views;

    /**
     * 省市
     */
    private Long provinceId;

    /**
     * 市区
     */
    private Long cityId;

    /**
     * 区县
     */
    private Long countyId;

    /**
     * 创建时间
     */
    private LocalDateTime createdTime;

    /**
     * 发布时间
     */
    private LocalDateTime publishTime;

    /**
     * 同步状态
     */
    private Boolean syncStatus;

    /**
     * 来源
     */
    private Integer origin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public Integer getLayout() {
        return layout;
    }

    public void setLayout(Integer layout) {
        this.layout = layout;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getLabels() {
        return labels;
    }

    public void setLabels(String labels) {
        this.labels = labels;
    }

    public Long getLikes() {
        return likes;
    }

    public void setLikes(Long likes) {
        this.likes = likes;
    }

    public Long getCollection() {
        return collection;
    }

    public void setCollection(Long collection) {
        this.collection = collection;
    }

    public Long getComment() {
        return comment;
    }

    public void setComment(Long comment) {
        this.comment = comment;
    }

    public Long getViews() {
        return views;
    }

    public void setViews(Long views) {
        this.views = views;
    }

    public Long getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Long provinceId) {
        this.provinceId = provinceId;
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public Long getCountyId() {
        return countyId;
    }

    public void setCountyId(Long countyId) {
        this.countyId = countyId;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public LocalDateTime getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(LocalDateTime publishTime) {
        this.publishTime = publishTime;
    }

    public Boolean getSyncStatus() {
        return syncStatus;
    }

    public void setSyncStatus(Boolean syncStatus) {
        this.syncStatus = syncStatus;
    }

    public Integer getOrigin() {
        return origin;
    }

    public void setOrigin(Integer origin) {
        this.origin = origin;
    }
}
