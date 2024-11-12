package com.vk.user.domain.dto;

import com.vk.user.domain.ApUser;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserInfoLogin extends ApUser {
    private String name;
}
