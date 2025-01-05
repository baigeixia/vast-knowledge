package com.vk.comment.common.utils;

import com.vk.common.core.exception.LeadNewsException;
import com.vk.common.core.utils.SensitiveWord;
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
    private SensitiveWord sensitiveWord;

    /**
     * 直接弹出
     */
    public void sensitiveDetectionThrow (String content){
        Map<String, AtomicInteger> stringAtomicIntegerMap = sensitiveWord.filterInfoShortText(content);
        if (!stringAtomicIntegerMap.isEmpty()) {
            Set<String> sensitiveS = stringAtomicIntegerMap.keySet();
            String result = String.join(",", sensitiveS);
            throw new LeadNewsException("评论内容内容违禁："+result);
        }
    }
    /**
     * 返回敏感词
     */
    public Map<String, AtomicInteger> sensitiveDetection (String content){
        Map<String, AtomicInteger> stringAtomicIntegerMap = sensitiveWord.filterInfoShortText(content);
        if (!stringAtomicIntegerMap.isEmpty()) {
          return stringAtomicIntegerMap;
        }
        return null;
    }
}
