package com.vk.article.domain.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ArticleDataListVo {
    private  Long page;
    private  Long size;
    private  Long total;
    private List<ArticleData> articleDataList=new ArrayList<>();
}
