package com.vk.user.service.user;


import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.row.Db;
import com.vk.common.core.constant.CacheConstants;
import com.vk.common.core.constant.UserBehaviourConstants;
import com.vk.common.core.constant.VisitorStatisticsConstant;
import com.vk.common.core.enums.UserType;
import com.vk.common.core.exception.LeadNewsException;
import com.vk.common.core.exception.ServiceException;
import com.vk.common.core.text.Convert;
import com.vk.common.core.utils.StringUtils;
import com.vk.common.core.utils.ip.IpUtils;
import com.vk.common.core.utils.ip.SearcherIpToAdder;
import com.vk.common.core.utils.uuid.UUID;
import com.vk.common.redis.service.RedisService;
import com.vk.common.security.utils.SecurityUtils;
import com.vk.system.model.LoginUser;
import com.vk.user.common.constant.ClientUserStatus;
import com.vk.user.domain.ApUser;
import com.vk.user.domain.ApUserInfo;
import com.vk.user.domain.ClientApUser;
import com.vk.user.domain.UserAndInfo;
import com.vk.user.domain.table.ApUserInfoTableDef;
import com.vk.user.mapper.ApUserInfoMapper;
import com.vk.user.mapper.ApUserMapper;
import com.vk.user.util.UpIpAddr;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import static com.vk.user.domain.table.ApUserInfoTableDef.AP_USER_INFO;

@Component
@Slf4j
public class UserLoginService {

    @Autowired
    private RedisService redisService;

    @Autowired
    private ApUserMapper apUserMapper;
    @Autowired
    private ApUserInfoMapper apUserInfoMapper;

    @Autowired
    private UpIpAddr upIpAddr;

    @Autowired
    private SearcherIpToAdder searcherIpToAdder;

    public LoginUser<ClientApUser> login(String token, String email, String password, Integer codeOrPas) {
        String redisToken = redisService.getCacheObject(getCacheTokenKey(token));
        if (StringUtils.isNotEmpty(redisToken)) {
            throw new ServiceException("重复提交");
        }

        boolean isTokenSet = redisService.setIfAbsent(getCacheTokenKey(token), token, CacheConstants.PAGE_TOKEN_TIME, TimeUnit.SECONDS);
        if (!isTokenSet) {
            throw new ServiceException("重复提交");
        }
        // IP黑名单校验
        String blackStr = Convert.toStr(redisService.getCacheObject(CacheConstants.SYS_LOGIN_BLACKIPLIST));
        if (IpUtils.isMatchedIp(blackStr, IpUtils.getIpAddr())) {
            throw new ServiceException("访问IP已被列入系统黑名单");
        }
        LoginUser<ClientApUser> resultVo = new LoginUser<>();

        if (UserBehaviourConstants.LOGIN.equals(codeOrPas)) {
            // 查询用户信息
            UserAndInfo byName = apUserMapper.getUserinfoByName(email);
            // 登录
            if (StringUtils.isNull(byName)) {
                throw new ServiceException("登录用户：" + email + " 不存在");
            }

            if (ClientUserStatus.CLIENT_DISABLE.getCode() == byName.getStatus()) {
                throw new ServiceException("对不起，您的账号：" + email + " 已停用");
            }

            validate(byName.getId(), byName.getPassword(), password, byName.getSalt());

            ClientApUser user = new ClientApUser();
            BeanUtils.copyProperties(byName, user);
            resultVo.setUsername(byName.getName());
            resultVo.setSysUser(user);



            resultVo.setUserid(byName.getId());
            resultVo.setUsername(byName.getName());
            resultVo.setMarkType(UserType.USER_TYPE.getType());

            String location = byName.getLocation();
            Long userid = byName.getId();
            //登录归属地改变，更新
            upIpAddr.checkAddress(location,userid);
            // redisAddUser(byName, resultVo);

            return resultVo;
        } else if (UserBehaviourConstants.REGISTER.equals(codeOrPas)) {
            ApUser user = apUserMapper.getUser(email);

            if (!StringUtils.isNull(user)) {
                throw new ServiceException("用户：" + email + " 已存在");
            }

            createNewUser(email, password,resultVo);

            try {
                redisService.incr(VisitorStatisticsConstant.getVisitorRegistrationsKey());
            } catch (Exception e) {
                log.error("reds add registrations error:{}",e.getMessage());
            }

            // redisAddUser(newUser, resultVo);
            return resultVo;
        } else {
            throw new LeadNewsException("错误请求");
        }
    }

    private void createNewUser(String email, String password,LoginUser<ClientApUser> resultVo) {
        // UserAndInfo userAndInfo = new UserAndInfo();
        LocalDateTime dateTime = LocalDateTime.now();

        ApUser user = new ApUser();
        user.setEmail(email);
        String salt = getSalt();
        user.setSalt(salt);
        user.setFlag(0);
        user.setImage("https://test-1316786270.cos.ap-guangzhou.myqcloud.com/article/406b9f3bc8914a60a4369acc7e77e03a.jpg");
        user.setCreatedTime(dateTime);
        user.setIsCertification(0);
        user.setIsIdentityAuthentication(0);
        user.setStatus(false);
        String encryptPassword = SecurityUtils.encryptPassword(password, salt);
        user.setPassword(encryptPassword);

        ApUserInfo info = new ApUserInfo();
        String ipAddr = IpUtils.getIpAddr();
        String province = searcherIpToAdder.SearcherToAdder(ipAddr);

        Db.tx(() -> {
            try {
                apUserMapper.insert(user);
                Long id = user.getId();
                info.setUserId(id);
                info.setName("MOMO");
                info.setSex(2);
                info.setLocation(province);
                info.setUpdatedTime(dateTime);
                apUserInfoMapper.insert(info);
            } catch (Exception e) {
                return false;
            }
            return true;
        });

        try {
            ClientApUser clientApUser = new ClientApUser();
            BeanUtils.copyProperties(user,clientApUser);
            resultVo.setUsername(info.getName());
            resultVo.setSysUser(clientApUser);
            // BeanUtils.copyProperties(userAndInfo, user);
            // BeanUtils.copyProperties(userAndInfo, info);
        } catch (Exception e) {
            log.error("复制用户失败");
        }
    }


    private String getSalt() {
        return UUID.generateCustomUUID(10, false);
    }


    private void validate(Long id, String oldPassword, String password, String salt) {
        Integer retryCount = redisService.getCacheObject(getCacheKey(id));

        if (retryCount == null) {
            retryCount = 0;
        }

        int maxRetryCount = CacheConstants.PASSWORD_MAX_RETRY_COUNT;
        Long lockTime = CacheConstants.PASSWORD_LOCK_TIME;
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

    private String getCacheTokenKey(String token) {
        return CacheConstants.PAGE_TOKEN + token;
    }

}
