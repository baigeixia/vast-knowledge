package com.vk.user.util;

import com.mybatisflex.core.query.QueryWrapper;
import com.vk.common.core.utils.StringUtils;
import com.vk.common.core.utils.ip.IpUtils;
import com.vk.common.core.utils.ip.SearcherIpToAdder;
import com.vk.user.domain.ApUserInfo;
import com.vk.user.mapper.ApUserInfoMapper;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.util.IPAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.vk.user.domain.table.ApUserInfoTableDef.AP_USER_INFO;

@Component
@Slf4j
public class UpIpAddr {
    @Autowired
    private ApUserInfoMapper apUserInfoMapper;
    @Autowired
    private SearcherIpToAdder searcherIpToAdder;

    /**
     * 不是原地址更新
     * @param location 之前地址
     * @param userId 对应账号
     */
    public void checkAddress(String location,Long userId){
        String ipAddr = IpUtils.getIpAddr();
        String province=null;
        try {
            province= searcherIpToAdder.SearcherToAdder(ipAddr);
            if (StringUtils.isEmpty(location) || !location.equals(province)) {
                ApUserInfo info = new ApUserInfo();
                info.setLocation(province);
                apUserInfoMapper.updateByQuery(info, QueryWrapper.create().where(AP_USER_INFO.USER_ID.eq(userId)));
            }
        } catch (Exception e) {
            log.error("用户端地址更新失败 userId:{},ipAddr:{},province:{}",userId, ipAddr,province);
        }
    }



}
