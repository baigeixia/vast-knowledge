package com.vk.common.mq.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NewMsg {
    private  Long notifyUserId;
    private  Integer messageType;

}
