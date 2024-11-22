package com.vk.dfs.template.impl;


import com.vk.dfs.enums.DFSType;
import com.vk.dfs.model.BaseFileModel;
import com.vk.dfs.template.AbstractDfsTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @version 1.0
 * @description 说明
 * @package com.itheima.dfs.template.impl
 */
@Component
@Slf4j
public class FastDfsTemplate extends AbstractDfsTemplate {
    @Override
    public DFSType support() {
        return DFSType.FASTDFS;
    }

    @Override
    public String uploadFile(BaseFileModel fileModel) {
        return null;
    }

    @Override
    public boolean delete(String fullPath) {
        return false;
    }

    @Override
    public List<byte[]> download(Collection<String> fullPath) {
        return null;
    }

    @Override
    public String getAccessServerAddr() {
        return null;
    }

    // @Autowired
    // private FastFileStorageClient client;
    //
    // @Autowired
    // private FdfsWebServer fdfsWebServer;
    //
    // /**
    //  * 定义支持的类型 必须设置值
    //  *
    //  * @return
    //  */
    // @Override
    // public DFSType support() {
    //     return DFSType.FASTDFS;
    // }
    //
    // /**
    //  * 上传文件
    //  *
    //  * @param fileModel
    //  * @return 文件的路径信息
    //  */
    // @Override
    // public String uploadFile(BaseFileModel fileModel) {
    //     HashSet<MetaData> metaData = new HashSet<>();
    //     //设置md5作为设置图片的签名
    //     metaData.add(new MetaData("md5", fileModel.getMd5()));
    //     ByteArrayInputStream bis = new ByteArrayInputStream(fileModel.getContent());
    //     StorePath storePath = client.uploadFile(
    //         bis,fileModel.getSize(),
    //         StringUtils.getFilenameExtension(fileModel.getName()),
    //         metaData);
    //     return storePath.getFullPath();
    // }
    //
    // /**
    //  * 删除文件
    //  *
    //  * @param fullPath
    //  */
    // @Override
    // public boolean delete(String fullPath) {
    //     try {
    //         client.deleteFile(fullPath);
    //     } catch (Exception e) {
    //         log.error("删除文件失败:{}",fullPath,e);
    //         return false;
    //     }
    //     return true;
    // }
    //
    // /**
    //  * 批量下载
    //  *
    //  * @param fullPath
    //  * @return
    //  */
    // @Override
    // public List<byte[]> download(Collection<String> fullPath) {
    //     List<byte[]> list = new ArrayList<>();
    //     // 下载
    //     if(!CollectionUtils.isEmpty(fullPath)){
    //         DownloadByteArray dba = new DownloadByteArray();
    //         list = fullPath.stream().map(fileId -> {
    //             // 解析文件id, 获取分组及文件目录
    //             StorePath storePath = StorePath.parseFromUrl(fileId);
    //             return client.downloadFile(storePath.getGroup(), storePath.getPath(),dba);
    //         }).collect(Collectors.toList());
    //     }
    //     return list;
    // }
    //
    // /**
    //  * 返回访问文件的服务器地址，不包含文件相对路径
    //  *
    //  * @return
    //  */
    // @Override
    // public String getAccessServerAddr() {
    //     return fdfsWebServer.getWebServerUrl();
    // }
}
