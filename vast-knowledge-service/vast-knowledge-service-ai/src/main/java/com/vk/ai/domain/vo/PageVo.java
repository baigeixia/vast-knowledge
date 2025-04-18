package com.vk.ai.domain.vo;

import lombok.Data;

import java.util.List;

@Data
public class PageVo<T> {
    private ChatInfoVo infoData;
    private List<T> content;
    private boolean hasNext;
    private int pageNumber;
    private int pageSize;
}