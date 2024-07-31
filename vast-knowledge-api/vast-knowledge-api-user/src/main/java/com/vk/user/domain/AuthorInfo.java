package com.vk.user.domain;

import lombok.Data;

@Data
public class AuthorInfo{
    private Long id;
    private String avatar;
    private String username;
    private String position;
}
