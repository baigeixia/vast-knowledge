package com.vk.user.domain.user;

import lombok.Data;

@Data
public class UserLoginBody {
    private String email;
    private String password;
    private Integer codeOrPas;
}
