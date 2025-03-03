package com.vk.article.domain.dto;

import com.vk.article.domain.ApArticleContent;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SaveArticleContentDto extends ApArticleContent {


    private String simpleDescription;
}
