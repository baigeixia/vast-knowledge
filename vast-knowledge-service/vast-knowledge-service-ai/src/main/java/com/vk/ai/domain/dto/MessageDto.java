package com.vk.ai.domain.dto;

import lombok.Data;

@Data
public class MessageDto {
    private  String model;
    private  String text;
    private  Object imageUrl;
}
