package com.vk.common.redis.constants;

import static com.vk.common.core.constant.CacheConstants.AD_CHANNEL_KEY;
import static com.vk.common.core.constant.CacheConstants.AD_CHANNEL_LABEL_KEY;

public class BusinessConstants {
    public static String loadingChannel(Long id){
        return AD_CHANNEL_KEY+id ;
    }

    public static String loadingLabel(Long id){
        return AD_CHANNEL_LABEL_KEY+id ;
    }


}
