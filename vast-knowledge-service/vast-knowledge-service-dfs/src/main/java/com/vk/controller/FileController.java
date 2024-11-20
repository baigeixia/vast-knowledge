package com.vk.controller;

import com.itheima.common.constants.SystemConstants;
import com.itheima.common.exception.LeadNewsException;
import com.itheima.common.util.RequestContextUtil;
import com.itheima.common.vo.ResultVo;
import com.itheima.dfs.enums.DFSType;
import com.itheima.dfs.model.BaseFileModel;
import com.itheima.dfs.strategy.DfsTemplateStrategyContext;
import com.itheima.dfs.template.DfsTemplate;
import com.itheima.wemedia.feign.WmMaterialFeign;
import com.itheima.wemedia.pojo.WmMaterial;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @version 1.0
 * @description 说明
 * @package com.itheima.controller
 */
@RestController
@RequestMapping("/dfs")
@Slf4j
public class FileController {

    /*@Resource(name="fastDfsTemplate")
    private FastDfsTemplate fastDfsTemplate;*/

    @Autowired
    private DfsTemplateStrategyContext dfsTemplateStrategyContext;

    @Value("${dfs.type}")
    private String dfsType; // 文件存储方案

    @Autowired
    private WmMaterialFeign wmMaterialFeign;

    /**
     * 上传文件
     * @param multipartFile
     * @return 文件的远程路径
     */
    //此参数名不修改，multipartFile。
    @PostMapping("/upload")
    public ResultVo upload(MultipartFile multipartFile){
        // 文件存储模板类, 通过支持的类型从策略池获取
        DfsTemplate dfsTemplate = dfsTemplateStrategyContext.getTemplate(DFSType.valueOf(dfsType));

        // 来源鉴权
        String origin = RequestContextUtil.getHeader(SystemConstants.USER_HEADER_FROM);
        log.info("文件来源: {},{}", origin,multipartFile.getOriginalFilename());
        if(StringUtils.isEmpty(origin)){
            throw new LeadNewsException("不正当来源，拒绝上传");
        }

        // 非空判断multipartFile
        // 获取上传文件的后缀名
        String ext = StringUtils.getFilenameExtension(multipartFile.getOriginalFilename());
        try {
            // 上传给fastdfs服务器
            /*StorePath storePath = client.uploadFile(multipartFile.getInputStream(), multipartFile.getSize(),
                ext, null);*/
            BaseFileModel baseFileModel = new BaseFileModel(
                "itheima",multipartFile.getSize(),multipartFile.getOriginalFilename(),
                multipartFile.getBytes()
                );
            String fullPath = dfsTemplate.uploadFile(baseFileModel);

            log.info("上传文件成功: {}", fullPath);
            // 远程调用自媒体 添加素材记录
            if(SystemConstants.WEMEDIA_PIC.equals(origin)){
                addWmMaterial(fullPath);
            }

            /**
             * {url:地址}
             */
            Map<String, String> result = Collections.singletonMap("url", dfsTemplate.getAccessServerAddr() + fullPath);
            return ResultVo.ok(result);
        } catch (IOException e) {
            log.error("上传文件失败",e);
        }
        return ResultVo.bizError("上传文件失败!");
    }

    /**
     * 批量下载文件
     * @param urls
     * @return
     */
    @PostMapping("/download")
    public ResultVo<List<byte[]>> downloadFiles(@RequestBody Collection<String> urls){
        // 获取客户端
        DfsTemplate dfsTemplate = dfsTemplateStrategyContext.getTemplate(DFSType.valueOf(dfsType));
        List<byte[]> list = dfsTemplate.download(urls);
        return ResultVo.ok(list);
    }

    /**
     * 远程调用自媒体 添加素材记录
     * @param fullPath
     */
    private void addWmMaterial(String fullPath) {
        //1. 构建pojo
        WmMaterial pojo = new WmMaterial();
        //2. 设置属性值
        pojo.setUrl(fullPath);
        pojo.setIsCollection(0);// 未收藏
        pojo.setType(0);//0=图片，添加常量
        pojo.setCreatedTime(LocalDateTime.now());
        //【注意】这里获取登录用户 报空指针，
        // 就要去检查自媒体网关的AuthorizeFilter方法对token解析后，是否存入登录用户id到请求头
        pojo.setUserId(RequestContextUtil.getUserId());
        //3. 远程调用
        ResultVo resultVo = wmMaterialFeign.addWmMaterial(pojo);
        //4. 结果解析
        if(!resultVo.isSuccess()){
            throw new LeadNewsException("上传文件失败");
        }
    }
}
