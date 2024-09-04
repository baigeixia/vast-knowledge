package com.vk.behaviour.common.utils.ws;

import static com.vk.common.redis.constants.BusinessConstants.SOCKET_USER_ID;

public class SocketConstants {
    public static String getRedisUserIdKey(Long userId) {
        return SOCKET_USER_ID + userId;
    }
}
