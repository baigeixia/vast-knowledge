package com.vk.user.service.user;


import com.vk.common.core.constant.CacheConstants;
import com.vk.common.core.enums.ClientUserStatus;
import com.vk.common.core.exception.ServiceException;
import com.vk.common.core.text.Convert;
import com.vk.common.core.utils.StringUtils;
import com.vk.common.core.utils.ip.IpUtils;
import com.vk.common.redis.service.RedisService;
import com.vk.common.security.utils.SecurityUtils;
import com.vk.user.domain.ClientApUser;
import com.vk.user.domain.UserAndInfo;
import com.vk.user.mapper.ApUserMapper;
import com.vk.user.model.LoginApUser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.vk.common.core.constant.UserConstants.PASSWORD_LOGIN;
import static com.vk.common.core.constant.UserConstants.VERIFICATION_CODE_LOGIN;

@Component
public class UserLoginService {

    @Autowired
    private RedisService redisService;

    @Autowired
    private ApUserMapper apUserMapper;

    private int maxRetryCount = CacheConstants.PASSWORD_MAX_RETRY_COUNT;
    private Long lockTime = CacheConstants.PASSWORD_LOCK_TIME;

    public LoginApUser login(String email, String password, String waitCode, Integer codeOrPas) {

        if (VERIFICATION_CODE_LOGIN == codeOrPas) {
            if (StringUtils.isAnyBlank(email, waitCode)) {
                throw new ServiceException("用户/验证码 必须填写");
            }
        }

        if (PASSWORD_LOGIN == codeOrPas) {
            if (StringUtils.isAnyBlank(email, password)) {
                throw new ServiceException("用户/密码 必须填写");
            }
        }

        // IP黑名单校验
        String blackStr = Convert.toStr(redisService.getCacheObject(CacheConstants.SYS_LOGIN_BLACKIPLIST));
        if (IpUtils.isMatchedIp(blackStr, IpUtils.getIpAddr())) {
            throw new ServiceException("很遗憾，访问IP已被列入系统黑名单");
        }

        // 查询用户信息
        UserAndInfo byName = apUserMapper.getUserinfoByName(email);
        if (StringUtils.isNull(byName)) {
            throw new ServiceException("登录用户：" + email + " 不存在");
        }

        if (Objects.equals(ClientUserStatus.CLIENT_DISABLE.getCode(), byName.getStatus())) {
            throw new ServiceException("对不起，您的账号：" + email + " 已停用");
        }

       validate(byName.getId(), byName.getPassword(), password,byName.getSalt());

        LoginApUser resultVo = new LoginApUser();
        ClientApUser user = new ClientApUser();
        BeanUtils.copyProperties(byName,user);
        resultVo.setUsername(byName.getName());
        resultVo.setClientApUser(user);

        return resultVo;
    }



    private  void validate(Long id,String oldPassword,String password,String salt){
        Integer retryCount = redisService.getCacheObject(getCacheKey(id));

        if (retryCount == null) {
            retryCount = 0;
        }

        if (retryCount >= maxRetryCount) {
            String errMsg = String.format("密码输入错误%s次，帐户锁定%s分钟", maxRetryCount, lockTime);
            throw new ServiceException(errMsg);
        }

        if (StringUtils.isNotEmpty(salt)) {
            if (!matches(oldPassword, password, salt)) {
                retryCount = retryCount + 1;
                redisService.setCacheObject(getCacheKey(id), retryCount, lockTime, TimeUnit.MINUTES);
                throw new ServiceException("用户不存在/密码错误");
            } else {
                clearLoginRecordCache(id);
            }

        } else {
            if (!matches(oldPassword, password)) {
                retryCount = retryCount + 1;
                redisService.setCacheObject(getCacheKey(id), retryCount, lockTime, TimeUnit.MINUTES);
                throw new ServiceException("用户不存在/密码错误");
            } else {
                clearLoginRecordCache(id);
            }
        }
    }

    public boolean matches(String oldPassword, String rawPassword, String salt) {
        String rawPasswordWithSalt = rawPassword + salt;
        return SecurityUtils.matchesPassword(rawPasswordWithSalt, oldPassword);
    }

    public boolean matches(String oldPassword, String rawPassword) {
        return SecurityUtils.matchesPassword(rawPassword, oldPassword);
    }

    public void clearLoginRecordCache(Long id) {
        if (redisService.hasKey(getCacheKey(id))) {
            redisService.deleteObject(getCacheKey(id));
        }
    }


    /**
     * 登录账户密码错误次数缓存键名
     *
     * @param id 用户id
     * @return 缓存键key
     */
    private String getCacheKey(Long id) {
        return CacheConstants.PWD_ERR_CNT_KEY + id;
    }

}
