package com.vk.comment.common.utils;

import com.vk.analyze.feign.RemoteChannelService;
import com.vk.common.core.domain.R;
import com.vk.common.core.domain.ValidationUtils;
import com.vk.common.core.exception.LeadNewsException;

import com.vk.common.redis.utlis.SensitiveWord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 敏感词检测
 */
@Component
public class SensitiveWordUtils {

    @Autowired
    private RemoteChannelService remoteChannelService;

    /**
     * 直接弹出
     */
    public void sensitiveDetectionThrow (String content){
        R<Map<String, AtomicInteger>> sensitiveShort = remoteChannelService.getSensitiveShort(content);
        if (ValidationUtils.validateRSuccess(sensitiveShort)) {
            Map<String, AtomicInteger> shortData = sensitiveShort.getData();
            if (!shortData.isEmpty()) {
                Set<String> sensitiveS = shortData.keySet();
                String result = String.join(",", sensitiveS);
                throw new LeadNewsException("评论内容内容违禁："+result);
            }
        }

    }
    /**
     * 返回敏感词
     */
    public Map<String, AtomicInteger> sensitiveDetection (String content){
        R<Map<String, AtomicInteger>> sensitiveShort = remoteChannelService.getSensitiveShort(content);
        if (ValidationUtils.validateRSuccess(sensitiveShort)) {
            Map<String, AtomicInteger> shortData = sensitiveShort.getData();
            if (!shortData.isEmpty()) {
                return shortData;
            }
        }

        return null;
    }
}
