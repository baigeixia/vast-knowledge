package com.vk.common.filter;

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
    private List<String> whites = new ArrayList<>();

    public void setWhites(List<String> whites)
    {
        this.whites = whites;
    }

    public List<String> getWhites() {
        whites.add("/dev-collection/analyze/channel/page");
        whites.add("/dev-core/article/article/homeList");
        whites.add("/dev-collection/behaviour/likes/articleLike");
        whites.add("/dev-core/search/search/searchInfo");
        whites.add("/dev-system/user/info/search");
        whites.add("/dev-core/article/Content/getInfo");
        whites.add("/dev-core/article/article/info");
        whites.add("/dev-core/comment/Comment/getCommentList");
        // whites.add("/dev-system/user/info/getInfo");
        whites.add("/dev-collection/behaviour/likes/commentLike/**");
        whites.add("/dev-collection/behaviour/behavior/list");
        // whites.add("/dev-system/user/info/InfoRelation");
        whites.add("/dev-core/article/article/posts");
        whites.add("/dev-collection/behaviour/collect/list");
        whites.add("/dev-system/user/follow/list");
        whites.add("/dev-system/user/fan/list");
        return whites;
    }
}
