package com.vk.dfs.template.impl;

import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.StorageClass;
import com.qcloud.cos.model.UploadResult;
import com.qcloud.cos.transfer.TransferManager;
import com.qcloud.cos.transfer.Upload;
import com.vk.dfs.enums.DFSType;
import com.vk.dfs.model.BaseFileModel;
import com.vk.dfs.template.AbstractDfsTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;

@Component
@Slf4j
public class TxDfsTemplate extends AbstractDfsTemplate {

    @Autowired
    private TransferManager transferManager;

    @Override
    public DFSType support() {
        return DFSType.TX;
    }

    @Override
    public String uploadFile(BaseFileModel fileModel) {
        // 对象键(Key)是对象在存储桶中的唯一标识。
        String fileModelName = fileModel.getName();
        // 存储桶的命名格式为 BucketName-APPID，此处填写的存储桶名称必须为此格式
        String bucketName = "test-1316786270";
        byte[] content = fileModel.getContent();

        PutObjectRequest putObjectRequest = getPutObjectRequest(content, bucketName, fileModelName);

        try {
            // 高级接口会返回一个异步结果Upload
            // 可同步地调用 waitForUploadResult 方法等待上传完成，成功返回 UploadResult, 失败抛出异常
            Upload upload = transferManager.upload(putObjectRequest);
            UploadResult uploadResult = upload.waitForUploadResult();
            System.out.println(uploadResult);
        } catch (CosServiceException e) {
            e.printStackTrace();
        } catch (CosClientException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static PutObjectRequest getPutObjectRequest(byte[] content, String bucketName, String key) {
        InputStream inputStream = new ByteArrayInputStream(content);

        ObjectMetadata objectMetadata = new ObjectMetadata();
        // 上传的流如果能够获取准确的流长度，则推荐一定填写 content-length
        // 如果确实没办法获取到，则下面这行可以省略，但同时高级接口也没办法使用分块上传了
        // 注意：流式上传不支持并发分块上传
        objectMetadata.setContentLength(content.length);

        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, inputStream, objectMetadata);
        // 设置存储类型（如有需要，不需要请忽略此行代码）, 默认是标准(Standard), 低频(standard_ia)
        // 更多存储类型请参见 https://cloud.tencent.com/document/product/436/33417
        putObjectRequest.setStorageClass(StorageClass.Standard_IA);
        return putObjectRequest;
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


}
