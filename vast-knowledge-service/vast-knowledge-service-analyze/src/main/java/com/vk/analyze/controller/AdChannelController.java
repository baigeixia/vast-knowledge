package com.vk.analyze.controller;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.vk.analyze.domain.AdChannel;
import com.vk.analyze.domain.table.AdChannelTableDef;
import com.vk.analyze.domain.vo.ChannelListVo;
import com.vk.analyze.service.AdChannelService;
import com.vk.common.core.domain.R;
import com.vk.common.core.exception.LeadNewsException;
import com.vk.common.core.utils.StringUtils;
import com.vk.common.core.web.controller.BaseController;
import com.vk.common.core.web.domain.AjaxResult;
import com.vk.common.core.web.page.PageDomain;
import com.vk.common.core.web.page.TableDataInfo;
import com.vk.common.core.web.page.TableSupport;
import com.vk.common.log.annotation.Log;
import com.vk.common.log.enums.BusinessType;
import com.vk.common.redis.constants.BusinessConstants;
import com.vk.common.redis.service.RedisService;
import com.vk.common.security.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import static com.vk.analyze.domain.table.AdChannelTableDef.AD_CHANNEL;
import static com.vk.common.core.constant.CacheConstants.AD_CHANNEL_KEY;

/**
 * 频道信息 控制层。
 *
 * @author 张三
 * @since 2024-05-13
 */
@RestController
@RequestMapping("/channel")
public class AdChannelController  {

    @Autowired
    private AdChannelService adChannelService;

    @Autowired
    private RedisService redisService;

    @GetMapping("getOneInfo/{channelId}")
    @RequiresPermissions("admin:channel:list")
    public R<AdChannel> getOneInfo(@PathVariable(name = "channelId") Long channelId) {
        AdChannel adChannel = redisService.getCacheObject(BusinessConstants.loadingChannel(channelId));
        if (ObjectUtils.isEmpty(adChannel)){
            AdChannel channel = adChannelService.getById(channelId);
            return  R.ok(channel);
        }
        return  R.ok(adChannel);
    }

    /**
     * 添加频道信息。
     *
     * @param adChannel 频道信息
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    @Log(title = "操作日志", businessType = BusinessType.INSERT)
    @RequiresPermissions("system:channel:add")
    public AjaxResult save(@RequestBody AdChannel adChannel) {
        String name = adChannel.getName();
        Integer ord = adChannel.getOrd();
        if (StringUtils.isEmpty(name)){
            throw new LeadNewsException("频道名称不能为空");
        }
        if (ObjectUtils.isEmpty(ord)){
            throw new LeadNewsException("频道排序不能为空");
        }

        AdChannel one = adChannelService.getOne(QueryWrapper.create().where(AD_CHANNEL.NAME.like(name)));
        if (!ObjectUtils.isEmpty(one)){
            throw new LeadNewsException("已经存在:"+one.getName());
        }

        adChannel.setCreatedTime(LocalDateTime.now());
        adChannel.setStatus(0);
        adChannel.setIsDefault(0);
        adChannelService.save(adChannel);

      redisService.setCacheObject(BusinessConstants.loadingChannel(adChannel.getId()),adChannel);

        return AjaxResult.success();
    }

    /**
     * 根据主键删除频道信息。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    @Log(title = "操作日志", businessType = BusinessType.DELETE)
    @RequiresPermissions("system:channel:del")
    public AjaxResult remove(@PathVariable(name = "id") Long  id) {
        adChannelService.removeByIdone(id);
        redisService.deleteObject(BusinessConstants.loadingChannel(id));
        return AjaxResult.success();
    }


    /**
     * 禁用  或者 启用。
     *
     * @param id 主键
     * @param isDisable  1 禁用 0 启用
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @RequiresPermissions("system:channel:up")
    @Log(title = "操作日志", businessType = BusinessType.UPDATE)
    @DeleteMapping("disable/{id}")
    public AjaxResult disable(
            @PathVariable(name = "id") Long  id,
            @RequestParam(name = "type")Integer isDisable
    ) {
        AdChannel service = adChannelService.getById(id);
        if (ObjectUtils.isEmpty(service)){
            throw new LeadNewsException("频道不存在 可能已被移除");
        }
        AdChannel channel = getChannel(isDisable, service);

        adChannelService.updateById(channel);
        redisService.setCacheObject(BusinessConstants.loadingChannel(id),channel);
        return AjaxResult.success();
    }

    private  AdChannel getChannel(Integer isDisable, AdChannel service) {
        AdChannel channel = new AdChannel();
        channel.setId(service.getId());
        Integer status = service.getStatus();
        if (1== isDisable){
            if(1==status){
                throw new LeadNewsException("频道已经禁用状态");
            }
            channel.setStatus(1);
        }else if (0== isDisable){
            if(0==status){
                throw new LeadNewsException("频道已经启用状态");
            }
            channel.setStatus(0);
        }else {
            throw new LeadNewsException("频道状态错误");
        }
        return channel;
    }

    /**
     * 根据主键更新频道信息。
     *
     * @param adChannel 频道信息
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @RequiresPermissions("system:channel:up")
    @Log(title = "操作日志", businessType = BusinessType.UPDATE)
    @PutMapping("update")
    public AjaxResult update(@RequestBody AdChannel adChannel) {
        Long id = adChannel.getId();
        if (StringUtils.isLongEmpty(id)){
            throw new LeadNewsException("频道id错误");
        }
        AdChannel service = adChannelService.getById(id);
        if (ObjectUtils.isEmpty(service)){
            throw new LeadNewsException("频道不存在 可能已被移除");
        }

        AdChannel channel = new AdChannel();
        channel.setId(adChannel.getId());
        channel.setName(adChannel.getName());
        channel.setOrd(adChannel.getOrd());

        adChannelService.updateById(channel);
        redisService.setCacheObject(BusinessConstants.loadingChannel(id),channel);
        return AjaxResult.success();
    }

    /**
     * 查询所有频道信息。
     *
     * @return 所有数据
     */
    @GetMapping ("list")
    public AjaxResult list(
    ) {
        List<ChannelListVo> list = adChannelService.listAs(QueryWrapper.create().select(AD_CHANNEL.NAME, AD_CHANNEL.ID), ChannelListVo.class);
        return AjaxResult.success(list);
    }

    /**
     * 分页查询频道信息。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @RequiresPermissions("admin:channel:list")
    @GetMapping("page")
    public AjaxResult page(
            @RequestParam(name = "page",defaultValue = "1",required = false) Long page ,
            @RequestParam(name = "size" ,defaultValue = "5",required = false) Long size,
            @RequestParam(name = "status" ,required = false) Integer status,
            @RequestParam(name = "name" ,required = false) String name
    ) {
        Page<AdChannel> paged = adChannelService.page(Page.of(page, size), QueryWrapper.create()
                .where(
                        AD_CHANNEL.STATUS.eq(status, !ObjectUtils.isEmpty(status))
                                .and(AD_CHANNEL.NAME.like(name, StringUtils.isNotEmpty(name)))
                )
                .orderBy(AD_CHANNEL.ORD, true));
        return AjaxResult.success(paged);
    }

}
