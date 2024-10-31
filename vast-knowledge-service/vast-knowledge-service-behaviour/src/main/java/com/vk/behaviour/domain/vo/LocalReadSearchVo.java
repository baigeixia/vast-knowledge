package com.vk.behaviour.domain.vo;


import lombok.Data;

import java.util.List;

@Data
public class LocalReadSearchVo  {
    private List<UserFootMarkListVo> footMarkLists;
    private Long totalHits;
}
