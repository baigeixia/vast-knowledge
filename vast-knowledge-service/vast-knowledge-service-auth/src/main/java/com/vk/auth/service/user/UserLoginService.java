package com.vk.auth.service.user;

import com.vk.common.core.constant.CacheConstants;
import com.vk.common.core.constant.Constants;
import com.vk.common.core.exception.ServiceException;
import com.vk.common.core.text.Convert;
import com.vk.common.core.utils.StringUtils;
import com.vk.common.core.utils.ip.IpUtils;
import com.vk.common.redis.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static com.vk.common.core.constant.UserConstants.PASSWORD_LOGIN;
import static com.vk.common.core.constant.UserConstants.VERIFICATION_CODE_LOGIN;

@Component
public class UserLoginService {

    @Autowired
    private RedisService redisService;

    public Map<String, Object> login(String email, String password, String waitCode,Integer codeOrPas) {

        if (VERIFICATION_CODE_LOGIN == codeOrPas){
            if (StringUtils.isAnyBlank(email, waitCode))
            {
                throw new ServiceException("用户/验证码 必须填写");
            }
        }
        if (PASSWORD_LOGIN == codeOrPas){
            if (StringUtils.isAnyBlank(email, password))
            {
                throw new ServiceException("用户/密码 必须填写");
            }
        }
        // IP黑名单校验
        String blackStr = Convert.toStr(redisService.getCacheObject(CacheConstants.SYS_LOGIN_BLACKIPLIST));
        if (IpUtils.isMatchedIp(blackStr, IpUtils.getIpAddr()))
        {
            throw new ServiceException("很遗憾，访问IP已被列入系统黑名单");
        }




        HashMap<String, Object> map = new HashMap<>();

        return map;
    }
}
