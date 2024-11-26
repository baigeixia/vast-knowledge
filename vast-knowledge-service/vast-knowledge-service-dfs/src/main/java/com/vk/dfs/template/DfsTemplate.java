package com.vk.dfs.template;


import com.vk.dfs.model.BaseFileModel;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * @version 1.0
 * @description 说明
 * @package com.itheima.dfs.service
 */
public interface DfsTemplate {

    /**
     * 上传文件
     *
     * @param fileModel
     * @return 文件的路径信息
     */
    String uploadFile(BaseFileModel fileModel);

    /**
     * 删除文件
     *
     * @param fullPath
     */
    boolean delete(String fullPath);

    /**
     * 批量下载
     * @param fullPath
     * @return
     */
    List<byte[]> download(Collection<String> fullPath) throws IOException;

    /**
     * 返回访问文件的服务器地址，不包含文件相对路径
     * @return
     */
    String getAccessServerAddr();


    void deleteList(Collection<String> urls);
}