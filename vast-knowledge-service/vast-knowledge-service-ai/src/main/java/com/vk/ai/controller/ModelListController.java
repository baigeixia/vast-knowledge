package com.vk.ai.controller;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.vk.ai.domain.ModelList;
import com.vk.ai.domain.vo.GetModelListVo;
import com.vk.ai.enums.AiType;
import com.vk.ai.enums.ModelListConstants;
import com.vk.ai.service.ModelListService;
import com.vk.common.core.exception.LeadNewsException;
import com.vk.common.core.utils.StringUtils;
import com.vk.common.core.web.domain.AjaxResult;
import com.vk.common.redis.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import static com.vk.ai.domain.table.ModelListTableDef.MODEL_LIST;

/**
 * 控制层。
 *
 * @author 张三
 * @since 2025-04-15
 */
@RestController
@RequestMapping("/modelList")
public class ModelListController {

    @Autowired
    private ModelListService modelListService;

    @Autowired
    private RedisService redisService;


    /**
     * 添加。
     *
     * @param modelList
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public AjaxResult save(@RequestBody ModelList modelList) {
        LocalDateTime dateTime = LocalDateTime.now();
        modelList.setCreatingTime(dateTime);
        modelList.setUpdateTime(dateTime);
         modelListService.save(modelList);

        return AjaxResult.success();
    }

    /**
     * 根据主键删除。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public AjaxResult remove(
            @PathVariable Serializable id,
            @RequestParam(name = "isState") Boolean isState,
            @RequestParam(name = "isDel") Boolean isDel
    ) {

        ModelList byId = modelListService.getById(id);
        if (null == byId) throw new LeadNewsException("错误的id");
        if (byId.getDel().equals(isDel)) throw new LeadNewsException("已经是:" + (isDel ? "删除" : "未删除") + "状态");
        if (byId.getState().equals(isState)) throw new LeadNewsException("已经是:" + (isState ? "启用" : "未启用") + "状态");

        ModelList modelList = new ModelList();
        modelList.setState(isState);
        modelList.setDel(isDel);

        modelListService.save(modelList);

        return AjaxResult.success();
    }

    /**
     * 根据主键更新。
     *
     * @param modelList
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public AjaxResult update(@RequestBody ModelList modelList) {
        String id = modelList.getModelId();
        if (StringUtils.isEmpty(id)) throw new LeadNewsException("id不能为空");

        ModelList byId = modelListService.getById(id);
        if (null == byId) throw new LeadNewsException("错误的id");

        String pertain = modelList.getPertain();

        if (Arrays.stream(AiType.values()).noneMatch(e -> e.name().equals(pertain))) {
           throw new RuntimeException("不支持的类型");
        }

        LocalDateTime dateTime = LocalDateTime.now();

        modelList.setState(null);
        modelList.setDel(null);
        modelList.setCreatingTime(null);
        modelList.setUpdateTime(dateTime);

        if (modelListService.updateById(modelList)) {
            String key = ModelListConstants.MODEL_LIST_ID;
            redisService.deleteObject(key);
        }

        return AjaxResult.success();
    }

    /**
     * 查询所有。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public AjaxResult list(
            @RequestParam(name = "offset",defaultValue = "1") Integer offset,
            @RequestParam(name = "limit",defaultValue = "10") Integer limit
    ) {
        String key = ModelListConstants.MODEL_LIST_ID;
        List<GetModelListVo> cacheList = redisService.getCacheList(key);

        if (cacheList.isEmpty()) {
            Page<GetModelListVo> listVoPage = modelListService.pageAs(Page.of(offset, limit),
                    QueryWrapper.create()
                            .where(MODEL_LIST.STATE.eq(true)
                            .and(MODEL_LIST.DEL.eq(false))
                            .and(MODEL_LIST.TOKEN_LIMIT.ne(0))),
                    GetModelListVo.class);

            List<GetModelListVo> records = listVoPage.getRecords();

            // 将所有结果缓存（这里可根据业务考虑是否分页缓存）
            redisService.setCacheObject(key, records);

            return AjaxResult.success(records);
        }

        // 处理分页逻辑
        int start = (offset - 1) * limit;
        int end = Math.min(start + limit, cacheList.size());

        if (start >= cacheList.size()) {
            return AjaxResult.success(Collections.emptyList());
        }

        List<GetModelListVo> pagedList = cacheList.subList(start, end);
        return AjaxResult.success(pagedList);
    }


}
