package com.vk.behaviour.service;

import com.mybatisflex.core.service.IService;
import com.vk.behaviour.domain.ApBehaviorEntry;
import com.vk.behaviour.domain.vo.BehaviorListVo;

import java.util.List;

/**
 * APP行为实体,一个行为实体可能是用户或者设备，或者其它 服务层。
 *
 * @author 张三
 * @since 2024-05-13
 */
public interface ApBehaviorEntryService extends IService<ApBehaviorEntry> {

    List<BehaviorListVo> getList(Long userId ,Long page,Long size);
}
