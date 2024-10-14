package com.vk.user.service.impl;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.row.Db;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.vk.common.core.exception.CustomSimpleThrowUtils;
import com.vk.common.core.exception.LeadNewsException;
import com.vk.common.core.utils.RequestContextUtil;
import com.vk.common.core.utils.StringUtils;
import com.vk.common.redis.service.RedisService;
import com.vk.user.domain.ApUser;
import com.vk.user.domain.ApUserInfo;
import com.vk.user.domain.dto.UserInfoDto;
import com.vk.user.domain.table.ApUserFollowTableDef;
import com.vk.user.domain.vo.InfoRelationVo;
import com.vk.user.domain.vo.LocalUserInfoVo;
import com.vk.user.domain.vo.UserInfoVo;
import com.vk.user.mapper.ApUserFollowMapper;
import com.vk.user.mapper.ApUserInfoMapper;
import com.vk.user.mapper.ApUserMapper;
import com.vk.user.service.ApUserInfoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

import static com.vk.user.common.constant.UserConstants.redisUserInfoKey;
import static com.vk.user.domain.table.ApUserFollowTableDef.AP_USER_FOLLOW;

/**
 * APP用户详情信息 服务层实现。
 *
 * @author 张三
 * @since 2024-05-13
 */
@Service
public class ApUserInfoServiceImpl extends ServiceImpl<ApUserInfoMapper, ApUserInfo> implements ApUserInfoService {

    @Autowired
    private RedisService redisService;
    @Autowired
    private ApUserMapper apUserMapper;

    @Autowired
    private ApUserFollowMapper apUserFollowMapper;
    @Override
    public void userConfig(Integer state, Integer type) {
        if (state.equals(1)) {
            ApUserInfo info = userVerification();
            if (info.getIsSendMessage().equals(type)) {
                throw new LeadNewsException("请勿重复修改");
            }
            info.setIsSendMessage(type);
            mapper.update(info);

        } else if (state.equals(2)) {
            ApUserInfo info = userVerification();
            if (info.getIsRecommendMe().equals(type)) {
                throw new LeadNewsException("请勿重复修改");
            }
            info.setIsRecommendMe(type);
            mapper.update(info);
        } else if (state.equals(3)) {
            ApUserInfo info = userVerification();
            if (info.getIsRecommendFriend().equals(type)) {
                throw new LeadNewsException("请勿重复修改");
            }
            info.setIsRecommendFriend(type);
            mapper.update(info);

        } else if (state.equals(4)) {
            ApUserInfo info = userVerification();
            if (info.getIsDisplayImage().equals(type)) {
                throw new LeadNewsException("请勿重复修改");
            }
            info.setIsDisplayImage(type);
            mapper.update(info);
        } else {
            throw new LeadNewsException("错误的类型");
        }

        Long userid = RequestContextUtil.getUserId();
        redisService.deleteObject(redisUserInfoKey(userid));
        UserInfoVo userInfoVo = mapper.selectGetInfo(userid);
        redisService.setCacheObject(redisUserInfoKey(userid),userInfoVo);
    }

    @Override
    public UserInfoVo getInfo(Long id) {
        Long  userid = RequestContextUtil.getUserId();
        if (StringUtils.isLongEmpty(id)){
            return userInfoIdOne(userid);
        }else {
            return userInfoIdOne(id);
        }
    }

    @Override
    public void upInfo(UserInfoDto dto) {
        CustomSimpleThrowUtils.ObjectIsEmpty(dto, "错误的参数");

        Long id = dto.getId();
        if (null==id){
            throw  new LeadNewsException("缺失参数");
        }

        String name = dto.getName();
        CustomSimpleThrowUtils.StringIsEmpty(name,"名称不能为空");

        LocalDate birthday = dto.getBirthday();
        ApUserInfo upInfo = new ApUserInfo();
        LocalDate now = LocalDate.now();
        if (null!=birthday){
            Period period = Period.between( birthday,now);
            int years = period.getYears();
            upInfo.setAge(years);
        }

        // ApUser apUser = apUserMapper.selectOneById(id);
        // if (null==apUser){
        //     throw  new LeadNewsException("错误的用户");
        // }

        Long userid = RequestContextUtil.getUserId();
        ApUserInfo info = mapper.selectOneByUserId(id);

        if (null==info){
            upInfo.setSex(2);
            BeanUtils.copyProperties(dto,upInfo);
            upInfo.setUserId(userid);
            upInfo.setFans(0L);
            upInfo.setFollows(0L);
            upInfo.setIsDisplayImage(0);
            upInfo.setIsRecommendMe(0);
            upInfo.setIsSendMessage(0);
            upInfo.setIsRecommendFriend(0);
            upInfo.setUpdatedTime(LocalDateTime.now());
            mapper.insert(upInfo);
        }else {
            if (!info.getId().equals(id)){
                throw  new LeadNewsException("没有权限修改");
            }

            BeanUtils.copyProperties(dto,upInfo);
            upInfo.setUpdatedTime(LocalDateTime.now());
            mapper.update(upInfo);

        }

        redisService.deleteObject(redisUserInfoKey(userid));
    }

    @Override
    public InfoRelationVo InfoRelation(Long id) {
        Long userid = RequestContextUtil.getUserId();
        if (StringUtils.isLongEmpty(id)){
            id=userid;
        }

        if (id.equals(userid)){
            return new InfoRelationVo(false);
        }

        long countByQuery = apUserFollowMapper.selectCountByQuery(QueryWrapper.create().where(AP_USER_FOLLOW.USER_ID.eq(userid).and(AP_USER_FOLLOW.FOLLOW_ID.eq(id))));
        if (countByQuery>0){
            return new InfoRelationVo(true);
        }

        return new InfoRelationVo(false);
    }

    private UserInfoVo userInfoIdOne(Long id){
        UserInfoVo redisUser= redisService.getCacheObject(redisUserInfoKey(id));
        if (null==redisUser) {
            ApUser apUser = apUserMapper.selectOneById(id);
            if (null == apUser) {
                throw new LeadNewsException("错误的用户");
            }
            UserInfoVo userInfoVo = mapper.selectGetInfo(id);
            userInfoVo.setPhone(maskPhoneNumber(userInfoVo.getPhone()));


            redisService.setCacheObject(redisUserInfoKey(id), userInfoVo);
            return userInfoVo;
        }

        return redisUser;
    }

    private ApUserInfo userVerification() {
        Long userid = RequestContextUtil.getUserId();
        ApUserInfo info = mapper.selectOneByUserId(userid);
        CustomSimpleThrowUtils.ObjectIsEmpty(info, "错误的用户");

        return info;
    }

    public static String maskPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.length() < 7) {
            return phoneNumber; // 如果手机号过短，直接返回
        }
        // 使用正则表达式替换手机号中间的部分为星号
        return phoneNumber.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    }
}
