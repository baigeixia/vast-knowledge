package com.vk.auth.service.user;

import com.vk.auth.service.system.SysPasswordService;
import com.vk.auth.service.system.SysRecordLogService;
import com.vk.common.core.constant.CacheConstants;
import com.vk.common.core.constant.Constants;
import com.vk.common.core.constant.SecurityConstants;
import com.vk.common.core.domain.R;
import com.vk.common.core.enums.ClientUserStatus;
import com.vk.common.core.exception.ServiceException;
import com.vk.common.core.text.Convert;
import com.vk.common.core.utils.StringUtils;
import com.vk.common.core.utils.ip.IpUtils;
import com.vk.common.redis.service.RedisService;
import com.vk.user.feign.RemoteClientUserService;
import com.vk.user.feign.domain.ClientApUser;
import com.vk.user.feign.model.LoginApUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static com.vk.common.core.constant.UserConstants.PASSWORD_LOGIN;
import static com.vk.common.core.constant.UserConstants.VERIFICATION_CODE_LOGIN;

@Component
public class UserLoginService {

    @Autowired
    private RedisService redisService;

    @Autowired
    private RemoteClientUserService remoteClientUserService;

    @Autowired
    private SysRecordLogService recordLogService;


    @Autowired
    private SysPasswordService passwordService;

    public LoginApUser login(String email, String password, String waitCode, Integer codeOrPas) {

        if (VERIFICATION_CODE_LOGIN == codeOrPas) {
            if (StringUtils.isAnyBlank(email, waitCode)) {
                recordLogService.recordLogininfor(email, Constants.LOGIN_FAIL, "用户/密码必须填写");
                throw new ServiceException("用户/验证码 必须填写");
            }
        }

        if (PASSWORD_LOGIN == codeOrPas) {
            if (StringUtils.isAnyBlank(email, password)) {
                recordLogService.recordLogininfor(email, Constants.LOGIN_FAIL, "用户/密码必须填写");
                throw new ServiceException("用户/密码 必须填写");
            }
        }

        // IP黑名单校验
        String blackStr = Convert.toStr(redisService.getCacheObject(CacheConstants.SYS_LOGIN_BLACKIPLIST));
        if (IpUtils.isMatchedIp(blackStr, IpUtils.getIpAddr())) {
            recordLogService.recordLogininfor(email, Constants.LOGIN_FAIL, "很遗憾，访问IP已被列入系统黑名单");
            throw new ServiceException("很遗憾，访问IP已被列入系统黑名单");
        }

        // 查询用户信息
        R<LoginApUser> userResult = remoteClientUserService.getUserInfo(email, SecurityConstants.INNER);

        if (StringUtils.isNull(userResult) || StringUtils.isNull(userResult.getData())) {
            recordLogService.recordLogininfor(email, Constants.LOGIN_FAIL, "登录用户不存在");
            throw new ServiceException("登录用户：" + email + " 不存在");
        }

        if (R.FAIL == userResult.getCode()) {
            throw new ServiceException(userResult.getMsg());
        }

        LoginApUser userInfo = userResult.getData();
        ClientApUser apUser = userInfo.getApUser();

        if (Objects.equals(ClientUserStatus.CLIENT_DISABLE.getCode(), apUser.getStatus())) {
            recordLogService.recordLogininfor(email, Constants.LOGIN_FAIL, "用户已停用，请联系管理员");
            throw new ServiceException("对不起，您的账号：" + email + " 已停用");
        }

        passwordService.validate(apUser.getName(), apUser.getPassword(), password,userInfo.getApUser().getSalt());
        recordLogService.recordLogininfor(email, Constants.LOGIN_SUCCESS, "登录成功");

        return userInfo;
    }


}
