package com.vk.wemedia.service.impl;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.vk.common.core.exception.LeadNewsException;
import com.vk.common.core.utils.RequestContextUtil;
import com.vk.wemedia.domain.WmMaterial;
import com.vk.wemedia.domain.table.WmMaterialTableDef;
import com.vk.wemedia.domain.table.WmNewsTableDef;
import com.vk.wemedia.mapper.WmMaterialMapper;
import com.vk.wemedia.service.WmMaterialService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static com.vk.wemedia.domain.table.WmMaterialTableDef.WM_MATERIAL;

/**
 * 自媒体图文素材信息 服务层实现。
 *
 * @author 张三
 * @since 2024-05-13
 */
@Service
public class WmMaterialServiceImpl extends ServiceImpl<WmMaterialMapper, WmMaterial> implements WmMaterialService {

    @Override
    public Page<WmMaterial> pageList(Integer type, Integer page, Integer size) {
        if (Stream.of(0,1).noneMatch(Predicate.isEqual(type))){
            throw new LeadNewsException("错误的类型");
        }
        Long userId = RequestContextUtil.getUserId();
        QueryWrapper wrapper = query().select(WM_MATERIAL.ID,WM_MATERIAL.URL,WM_MATERIAL.IS_COLLECTION).where(
                WM_MATERIAL.USER_ID.eq(userId)
                        //1=收藏 默认查询
                        .and(WM_MATERIAL.IS_COLLECTION.eq(type, type.equals(1)))
                        .and(WM_MATERIAL.DEL.eq(0))
        ).orderBy(WM_MATERIAL.CREATED_TIME, false);

        return mapper.paginate(Page.of(page,size), wrapper);
    }
}
