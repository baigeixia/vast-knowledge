package com.vk.common.core.exception.auth;

import java.io.Serial;

public class NotConfigUnknownException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public NotConfigUnknownException(String message)
    {
        super(message);
    }

}
