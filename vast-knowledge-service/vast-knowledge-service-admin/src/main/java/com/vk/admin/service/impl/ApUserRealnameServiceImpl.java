package com.vk.admin.service.impl;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.vk.admin.domain.ApUserRealname;
import com.vk.admin.domain.dto.ApUserRealnameDto;
import com.vk.admin.mapper.ApUserRealnameMapper;
import com.vk.admin.service.ApUserRealnameService;
import org.springframework.stereotype.Service;

import static com.vk.admin.domain.table.ApUserRealnameTableDef.AP_USER_REALNAME;

/**
 * APP实名认证信息 服务层实现。
 *
 * @author 张三
 * @since 2024-05-13
 */
@Service
public class ApUserRealnameServiceImpl extends ServiceImpl<ApUserRealnameMapper, ApUserRealname> implements ApUserRealnameService {

    @Override
    public Page<ApUserRealname> getlist(ApUserRealnameDto dto) {
       return this.mapper.paginateWithRelations(dto.getPage(),dto.getSize(),query().and(AP_USER_REALNAME.STATUS.eq(dto.getStatus())));
    }
}
