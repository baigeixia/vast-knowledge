package com.vk.dfs.template.impl;

import com.qcloud.cos.COS;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.exception.MultiObjectDeleteException;
import com.qcloud.cos.model.*;
import com.qcloud.cos.transfer.TransferManager;
import com.qcloud.cos.transfer.Upload;
import com.qcloud.cos.utils.IOUtils;
import com.vk.common.core.domain.R;
import com.vk.common.core.domain.ValidationUtils;
import com.vk.common.core.exception.LeadNewsException;
import com.vk.common.core.utils.RequestContextUtil;
import com.vk.common.core.utils.threads.TaskVirtualExecutorUtil;
import com.vk.config.TxDfsConfig;
import com.vk.dfs.enums.DFSType;
import com.vk.dfs.enums.FilePosition;
import com.vk.dfs.model.BaseFileModel;
import com.vk.dfs.template.AbstractDfsTemplate;
import com.vk.user.feign.RemoteClientUserService;
import com.vk.wemedia.domain.WmMaterialFeign;
import com.vk.wemedia.feign.RemoteClientWemediaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Slf4j
public class TxDfsTemplate extends AbstractDfsTemplate {

    @Autowired
    private TransferManager transferManager;

    @Autowired
    private RemoteClientWemediaService remoteClientWemediaService;

    @Autowired
    private RemoteClientUserService remoteClientUserService;

    @Autowired
    private TxDfsConfig txDfsConfig;

    @Override
    public DFSType support() {
        return DFSType.TX;
    }

    private final String bucketName = "test-1316786270";

    @Override
    public String uploadFile(BaseFileModel fileModel) {
        // 对象键(Key)是对象在存储桶中的唯一标识。
        String fileModelName = fileModel.getName();
        String origin = fileModel.getOrigin();
        // 存储桶的命名格式为 BucketName-APPID，此处填写的存储桶名称必须为此格式
        byte[] content = fileModel.getContent();

        Long userId = RequestContextUtil.getUserId();


        return TaskVirtualExecutorUtil.executeWith(() ->{
            try {
                // 高级接口会返回一个异步结果Upload
                // 可同步地调用 waitForUploadResult 方法等待上传完成，成功返回 UploadResult, 失败抛出异常
                PutObjectRequest putObjectRequest = getPutObjectRequest(content, bucketName, fileModelName);
                Upload upload = transferManager.upload(putObjectRequest);

                UploadResult uploadResult = upload.waitForUploadResult();
                String path = uploadResult.getKey();
                TaskVirtualExecutorUtil.executeWith(() -> syncUpImag(origin, path,userId));

                return path;
            } catch (CosServiceException e) {
                log.error("Cos 服务异常 - 文件：{} 上传失败，原因：{}", fileModelName, e.getMessage());
                throw new LeadNewsException("网络异常 上传失败");
            } catch (CosClientException e) {
                log.error("Cos 客户端异常 - 文件：{} 上传失败，原因：{}", fileModelName, e.getMessage());
                throw new LeadNewsException("连接失败 上传失败");
            } catch (InterruptedException e) {
                log.error("Cos 上传中断 - 文件：{} 上传失败，原因：{}", fileModelName, e.getMessage());
                throw new LeadNewsException("连接中断 上传失败");
            }
        });

    }

    private void syncUpImag(String origin, String path,Long userId) {
        String host = txDfsConfig.getHost();
        path=host+path;

        FilePosition filePosition = null;
        try {
            filePosition = FilePosition.valueOf(origin.toUpperCase());
        } catch (IllegalArgumentException e) {
            log.error("Invalid origin: {}", origin);
            return;
        }
        switch (filePosition) {
            case ARTICLE -> {
                // 文章
                upArticle(path,userId);
            }
            case AVATAR -> {
                // 头像
                upAvatar(path,userId);
            }
            case COMMENT -> {
                // 评论

            }
        }
    }

    private void upAvatar(String path,Long userId) {
        remoteClientUserService.upImage(path,userId);
    }

    private void upArticle(String path,Long userId) {
        WmMaterialFeign feign = new WmMaterialFeign();
        feign.setUrl(path);
        feign.setUserId(1L);
        feign.setType(0);
        feign.setIsCollection(false);
        feign.setCreatedTime(LocalDateTime.now());
        R<Boolean> booleanR = remoteClientWemediaService.saveMaterial(feign);
        ValidationUtils.validateR(booleanR, "上传图片失败");
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
        COS cosClient = transferManager.getCOSClient();
        try {
            fullPath = getObjectKeyFromUrl(fullPath);
            cosClient.deleteObject(bucketName, fullPath);
        }catch (CosServiceException e) {
            log.error("Cos 服务异常 - 文件：{} 上传失败，原因：{}", fullPath, e.getMessage());
            throw new LeadNewsException("网络异常 上传失败");
        } catch (CosClientException e) {
            log.error("Cos 客户端异常 - 文件：{} 上传失败，原因：{}", fullPath, e.getMessage());
            throw new LeadNewsException("连接失败 上传失败");
        }
        return false;
    }

    @Override
    public List<byte[]> download(Collection<String> fullPath) {
        COS cosClient = transferManager.getCOSClient();
        return fullPath.stream().map(p -> {
            String key = getObjectKeyFromUrl(p);
            GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, key);
            InputStream cosObjectInput = null;
            try {
                COSObject cosObject = cosClient.getObject(getObjectRequest);
                cosObjectInput = cosObject.getObjectContent();
            } catch (CosServiceException e) {
                log.error("Cos 服务异常 - 文件：{} 上传失败，原因：{}", p, e.getMessage());
                throw new LeadNewsException("网络异常 下载失败");
            } catch (CosClientException e) {
                log.error("Cos 客户端异常 - 文件：{} 上传失败，原因：{}", p, e.getMessage());
                throw new LeadNewsException("连接失败 下载失败");
            }
            // 处理下载到的流
            // 这里是直接读取，按实际情况来处理
            byte[] bytes = null;
            try {
                bytes = IOUtils.toByteArray(cosObjectInput);
            } catch (IOException e) {
                log.error("文件读取失败 - 文件：{} 上传失败，原因：{}", p, e.getMessage());
                throw new LeadNewsException("文件读取失败 下载失败");
            } finally {
                // 用完流之后一定要调用 close()
                try {
                    cosObjectInput.close();
                } catch (IOException e) {
                    log.error("文件读取失败 - 文件：{} 上传失败，原因：{}", p, e.getMessage());
                }
            }

            return bytes;
        }).toList();
    }

    @Override
    public String getAccessServerAddr() {
        return txDfsConfig.getHost();
    }

    @Override
    public void deleteList(Collection<String> urls) {
        COS cosClient = transferManager.getCOSClient();

        DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(bucketName);
// 设置要删除的key列表, 最多一次删除1000个

        if (urls.size() > 1000) {
            throw new LeadNewsException("选中内容超出范围");
        }

        // 传入要删除的文件名
// 注意文件名不允许以正斜线/或者反斜线\开头，例如：
// 存储桶目录下有a/b/c.txt文件，如果要删除，只能是 keyList.add(new KeyVersion("a/b/c.txt")),
// 若使用 keyList.add(new KeyVersion("/a/b/c.txt"))会导致删除不成功
        List<DeleteObjectsRequest.KeyVersion> keyList = urls.stream().map(i -> {
            String key = getObjectKeyFromUrl(i);
            return new DeleteObjectsRequest.KeyVersion(key);
        }).toList();

        deleteObjectsRequest.setKeys(keyList);

        try {
            DeleteObjectsResult deleteObjectsResult = cosClient.deleteObjects(deleteObjectsRequest);
            List<DeleteObjectsResult.DeletedObject> deleteObjectResultArray = deleteObjectsResult.getDeletedObjects();
            System.out.println(deleteObjectResultArray);
        } catch (MultiObjectDeleteException mde) {
            // 如果部分删除成功部分失败, 返回 MultiObjectDeleteException
            List<DeleteObjectsResult.DeletedObject> deleteObjects = mde.getDeletedObjects();
            List<MultiObjectDeleteException.DeleteError> deleteErrors = mde.getErrors();
        } catch (CosServiceException e) {
            log.error("Cos 服务异常 -  上传失败，原因：{}", e.getMessage());
            throw new LeadNewsException("网络异常 删除失败");
        } catch (CosClientException e) {
            log.error("Cos 客户端异常 - 上传失败，原因：{}", e.getMessage());
            throw new LeadNewsException("连接失败 删除失败");
        }
    }

    private String getObjectKeyFromUrl(String url) {
        // 正则表达式匹配 URL 中的对象路径部分
        String regex = "^(?:https://)?[^/]+/(.*)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(url);

        // 如果匹配成功，返回路径部分
        if (matcher.find()) {
            return matcher.group(1);  // 返回匹配到的路径部分
        } else {
            return null;  // 如果URL格式不正确，返回null
        }
    }


}
