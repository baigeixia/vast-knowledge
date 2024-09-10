package com.vk.user.domain.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MsgUserInfo {
    private Long id;
    private String name;
    private String avatar;
}
