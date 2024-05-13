package com.vk.admin.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import com.vk.admin.domain.ApUserRealname;
import com.vk.admin.domain.dto.ApUserRealnameDto;

/**
 * APP实名认证信息 服务层。
 *
 * @author 张三
 * @since 2024-05-13
 */
public interface ApUserRealnameService extends IService<ApUserRealname> {

    Page<ApUserRealname> getlist(ApUserRealnameDto dto);
}
