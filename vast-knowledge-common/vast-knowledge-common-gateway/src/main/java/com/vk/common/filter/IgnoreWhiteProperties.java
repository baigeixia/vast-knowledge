package com.vk.common.filter;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * 放行白名单配置
 * 
 * @author vk
 */
@Configuration
@RefreshScope
@ConfigurationProperties(prefix = "security.ignore")
public class IgnoreWhiteProperties
{
    /**
     * 放行白名单配置，网关不校验此处的白名单
     */
    private List<String> whites ;
    /**
     * 验证码 验证 获取路径
     */
    private List<String> checkCode=new ArrayList<>() ;
    @PostConstruct
    private void init(){
        initializeWhites();
    }
    private void initializeWhites() {
        whites.add("/analyze/channel/page");
        whites.add("/article/article/homeList");
        // whites.add("/behaviour/likes/articleLike");
        whites.add("/search/search/searchInfo");
        whites.add("/user/info/search");
        whites.add("/article/Content/getInfo");
        whites.add("/article/article/info");
        whites.add("/comment/Comment/getCommentList");
        // whites.add("/user/info/getInfo");
//        whites.add("/behaviour/likes/commentLike/**");
        whites.add("/behaviour/behavior/list");
        // whites.add("/user/info/InfoRelation");
        whites.add("/article/article/posts");
        whites.add("/behaviour/collect/list");
        whites.add("/user/follow/list");
        whites.add("/user/fan/list");

        whites.add("/user/login");
        whites.add("/user/refresh");
        whites.add("/user/logout");

        whites.add("/system/login");
        whites.add("/system/logout");
        whites.add("/system/refresh");


        checkCode.add("/system/login");
        checkCode.add("/user/login");
    }

    public List<String> getCheckCode() {
        return this.checkCode;
    }

    public void setCheckCode(List<String> checkCode) {
        this.checkCode = checkCode;
    }

    public void setWhites(List<String> whites)
    {
        this.whites = whites;
    }


    public List<String> getWhites() {
        return this.whites;
    }
}
