package com.vk.article.domain.dto;

import com.vk.article.domain.ApArticleContent;
import lombok.Data;

@Data
public class SaveArticleContentDto extends ApArticleContent {

    private String simpleDescription;
}
