package com.vk.behaviour.domain.vo;


import com.vk.article.domain.HomeArticleListVo;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class UserFootMarkListVo {
    private LocalDateTime dateTime;
    private List<HomeArticleListVo> footMark=new ArrayList<>();
}
