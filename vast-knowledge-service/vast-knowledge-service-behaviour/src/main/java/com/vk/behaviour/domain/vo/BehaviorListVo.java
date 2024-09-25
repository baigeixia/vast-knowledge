package com.vk.behaviour.domain.vo;

import com.vk.article.domain.HomeArticleListVo;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BehaviorListVo {
    private String actionText;
    private LocalDateTime createdTime;
    private HomeArticleListVo target;
}
