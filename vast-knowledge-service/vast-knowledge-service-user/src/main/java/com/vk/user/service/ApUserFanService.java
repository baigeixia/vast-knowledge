package com.vk.user.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import com.vk.user.domain.ApUserFan;
import com.vk.user.domain.vo.FanListVo;

/**
 * APP用户粉丝信息 服务层。
 *
 * @author 张三
 * @since 2024-05-13
 */
public interface ApUserFanService extends IService<ApUserFan> {

    Page<FanListVo> getList(Long page, Long size, Long userId);
}
