package com.vk.wemedia.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import com.vk.wemedia.domain.WmMaterial;

/**
 * 自媒体图文素材信息 服务层。
 *
 * @author 张三
 * @since 2024-05-13
 */
public interface WmMaterialService extends IService<WmMaterial> {

    Page<WmMaterial> pageList(Integer type, Integer page, Integer size);
}
