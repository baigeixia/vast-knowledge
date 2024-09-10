package com.vk.user.domain.vo;

import lombok.Data;

import java.util.List;

@Data
public class MsgListVo {
   private List<MsgInfo> messages;
   private MsgUserInfo receiver;
   private MsgUserInfo sender;

}
