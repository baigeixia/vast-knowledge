package com.vk.common.core.enums;


public enum UserType {
    ADMIN_TYPE("admin:"),
    USER_TYPE("user:");

    private final String type;

    UserType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
