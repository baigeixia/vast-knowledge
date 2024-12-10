package com.vk.article.domain.vo;


import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ArticleDataVo {

    /**
     * 图文发布量
     */
    private  Long release;
    private  Long like;
    private  Long collect;
    private  Long views;
    private List<ArticleData> articleDataList=new ArrayList<>();

}
