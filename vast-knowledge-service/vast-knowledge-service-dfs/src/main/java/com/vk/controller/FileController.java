package com.vk.controller;


import com.alibaba.nacos.common.utils.UuidUtils;
import com.vk.common.core.domain.R;
import com.vk.common.core.domain.ValidationUtils;
import com.vk.common.core.exception.LeadNewsException;
import com.vk.common.core.utils.RequestContextUtil;
import com.vk.common.core.utils.threads.TaskVirtualExecutorUtil;
import com.vk.common.core.utils.uuid.UUID;
import com.vk.common.core.web.domain.AjaxResult;
import com.vk.config.DfsConfig;
import com.vk.config.TxDfsConfig;
import com.vk.dfs.enums.DFSType;
import com.vk.dfs.enums.FilePosition;
import com.vk.dfs.model.BaseFileModel;
import com.vk.dfs.strategy.DfsTemplateStrategyContext;
import com.vk.dfs.template.DfsTemplate;
import com.vk.wemedia.domain.WmMaterialFeign;
import com.vk.wemedia.feign.RemoteClientWemediaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Predicate;

/**
 * @version 1.0
 * @description 说明
 * @package com.itheima.controller
 */
@RestController
@RequestMapping("/dfs")
@Slf4j
// @CrossOrigin
public class FileController {

    @Autowired
    private DfsTemplateStrategyContext dfsTemplateStrategyContext;

    @Autowired
    private DfsConfig dfsConfig;

    @Autowired
    private TxDfsConfig txDfsConfig;

    @PostMapping("/upload")
    public AjaxResult upload(
            @RequestParam("file") MultipartFile multipartFile
    ) {
        if (multipartFile.isEmpty()){
            throw new LeadNewsException("上传内容不能为空");
        }
        if (multipartFile.getSize() > 20*1024*1024){
            throw new LeadNewsException("上传内容不能大于 20MB");
        }

        // 文件存储模板类, 通过支持的类型从策略池获取
        DfsTemplate dfsTemplate = dfsTemplateStrategyContext.getTemplate(DFSType.TX);

        // 来源鉴权
        String origin = RequestContextUtil.getHeader("from");
        log.info("文件来源: {},{}", origin,multipartFile.getOriginalFilename());
        if(ObjectUtils.isEmpty(origin)){
            throw new LeadNewsException("不正当来源，拒绝上传");
        }

        if(Arrays.stream(FilePosition.values())
                .noneMatch(filePosition -> filePosition.name().equals(origin.toUpperCase()))){
            throw new RuntimeException("来源无法识别");
        }

        // 非空判断multipartFile  url  图片文件夹/图片名称+时间戳+后缀
        String filename = multipartFile.getOriginalFilename();
        if (StringUtils.hasText(filename)){
            // 获取上传文件的后缀名
            String ext = StringUtils.getFilenameExtension(filename);
            if (dfsConfig.getMatchExt().stream().noneMatch(Predicate.isEqual(ext))){
                throw new LeadNewsException("不支持的文件类型");
            }
            String replace = UUID.randomUUID().toString().replace("-", "");
            filename = origin+"/"+replace + "." + ext;
        }

        try {
            // 上传给fastdfs服务器
            BaseFileModel baseFileModel = new BaseFileModel(
                    "zhangsan",
                    multipartFile.getSize(),
                    filename,
                    multipartFile.getBytes(),
                    origin
            );
            String fullPath = dfsTemplate.uploadFile(baseFileModel);
            log.info("上传文件成功: {}", fullPath);

            /**
             * {url:地址}
             */
            Map<String, String> result = Collections.singletonMap("url", txDfsConfig.getHost()+fullPath);
            return AjaxResult.success(result);
        } catch (IOException e) {
            log.error("上传文件失败", e);
        }
        return AjaxResult.error("上传文件失败!");
    }

    /**
     * 批量下载文件
     * @param urls
     * @return
     */
    @PostMapping("/download")
    public AjaxResult downloadFiles(@RequestBody Collection<String> urls){
        // 获取客户端
        DfsTemplate dfsTemplate = dfsTemplateStrategyContext.getTemplate(DFSType.TX);
        List<byte[]> list = null;
        try {
            list = dfsTemplate.download(urls);
        } catch (IOException e) {
            throw new LeadNewsException("下载异常");
        }
        return AjaxResult.success(list);
    }

    @DeleteMapping("/delete")
    public AjaxResult delete(@RequestParam(name = "url")String url){
        // 获取客户端
        DfsTemplate dfsTemplate = dfsTemplateStrategyContext.getTemplate(DFSType.TX);
        dfsTemplate.delete(url);
        return AjaxResult.success();
    }

    @DeleteMapping("/deleteList")
    public AjaxResult deleteList(@RequestBody Collection<String> urls){
        // 获取客户端
        DfsTemplate dfsTemplate = dfsTemplateStrategyContext.getTemplate(DFSType.TX);
        dfsTemplate.deleteList(urls);
        return AjaxResult.success();
    }

}
