package cn.newness.imip.module.infra.framework.file.core.client.s3;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.newness.imip.framework.web.core.util.WebFrameworkUtils;
import cn.newness.imip.module.infra.framework.file.config.UploadProgressManager;
import cn.newness.imip.module.infra.framework.file.core.client.AbstractFileClient;
import cn.newness.imip.module.infra.framework.file.core.client.ProgressInputStream;
import cn.newness.imip.module.infra.framework.file.core.client.ProgressListener;
import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import io.minio.messages.Item;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * 基于 S3 协议的文件客户端，实现 MinIO、阿里云、腾讯云、七牛云、华为云等云服务
 * <p>
 * S3 协议的客户端，采用亚马逊提供的 software.amazon.awssdk.s3 库
 *
 * @author 新奇源码
 */
public class S3FileClient extends AbstractFileClient<S3FileClientConfig> {

    private static final Logger log = LoggerFactory.getLogger(S3FileClient.class);
    private MinioClient client;

    public S3FileClient(Long id, S3FileClientConfig config) {
        super(id, config);
    }

    @Override
    protected void doInit() {
        // 补全 domain
        if (StrUtil.isEmpty(config.getDomain())) {
            config.setDomain(buildDomain());
        }
        // 初始化客户端
        client = MinioClient.builder()
                .endpoint(buildEndpointURL()) // Endpoint URL
                .region(buildRegion()) // Region
                .credentials(config.getAccessKey(), config.getAccessSecret()) // 认证密钥
                .build();
    }

    /**
     * 基于 endpoint 构建调用云服务的 URL 地址
     *
     * @return URI 地址
     */
    private String buildEndpointURL() {
        // 如果已经是 http 或者 https，则不进行拼接.主要适配 MinIO
        if (HttpUtil.isHttp(config.getEndpoint()) || HttpUtil.isHttps(config.getEndpoint())) {
            return config.getEndpoint();
        }
        return StrUtil.format("https://{}", config.getEndpoint());
    }

    /**
     * 基于 bucket + endpoint 构建访问的 Domain 地址
     *
     * @return Domain 地址
     */
    private String buildDomain() {
        // 如果已经是 http 或者 https，则不进行拼接.主要适配 MinIO
        if (HttpUtil.isHttp(config.getEndpoint()) || HttpUtil.isHttps(config.getEndpoint())) {
            return StrUtil.format("{}/{}", config.getEndpoint(), config.getBucket());
        }
        // 阿里云、腾讯云、华为云都适合。七牛云比较特殊，必须有自定义域名
        return StrUtil.format("https://{}.{}", config.getBucket(), config.getEndpoint());
    }

    /**
     * 基于 bucket 构建 region 地区
     *
     * @return region 地区
     */
    private String buildRegion() {
        // 阿里云必须有 region，否则会报错
        if (config.getEndpoint().contains(S3FileClientConfig.ENDPOINT_ALIYUN)) {
            return StrUtil.subBefore(config.getEndpoint(), '.', false)
                    .replaceAll("-internal", "")// 去除内网 Endpoint 的后缀
                    .replaceAll("https://", "");
        }
        // 腾讯云必须有 region，否则会报错
        if (config.getEndpoint().contains(S3FileClientConfig.ENDPOINT_TENCENT)) {
            return StrUtil.subAfter(config.getEndpoint(), "cos.", false)
                    .replaceAll("." + S3FileClientConfig.ENDPOINT_TENCENT, ""); // 去除 Endpoint
        }
        return null;
    }

    @Override
    public String upload(byte[] content, String path, String type) throws Exception {
        // 执行上传
        client.putObject(PutObjectArgs.builder()
                .bucket(config.getBucket()) // bucket 必须传递
                .contentType(type)
                .object(path) // 相对路径作为 key
                .stream(new ByteArrayInputStream(content), content.length, -1) // 文件内容
                .build());
        // 拼接返回路径
        return config.getDomain() + "/" + path;
    }

    @Override
    public String upload(BufferedInputStream inputStream, String path, String type, long fileLength, UploadProgressManager uploadProgressManager, String userId) throws Exception {
        String url="文件上传失败";
        //实现监听函数
        ProgressListener listener=(byteRead,totalBytes)->{
            uploadProgressManager.updateProgress(userId,byteRead,totalBytes);
            log.info(path+"上传进度："+uploadProgressManager.getProgress(userId)+"%");
        };
        // 执行上传【每次read都会更新进度】
        ProgressInputStream progressInputStream=new ProgressInputStream(inputStream,fileLength,listener);
        log.info(path+"文件大小是："+fileLength);
        client.putObject(PutObjectArgs.builder()
                .bucket(config.getBucket()) // bucket 必须传递
                .contentType(type)
                .object(path) // 相对路径作为 key
                .stream(progressInputStream, fileLength, -1) // 文件内容
                .build());
        try{
            int read = progressInputStream.read();
            log.info(path+"流的读取位置："+read);
            if(read==-1){
                url=config.getDomain() + "/" + path;
            }
        }catch (IOException e){
            log.info(path+"流关闭了"+e.getMessage());
        }

        //上传完成后删除存储的此条进度
        uploadProgressManager.removeProgress(userId);
        log.info(path+"上传完成的进度是："+uploadProgressManager.getProgress(userId)+"%");
        // 拼接返回路径
        return url;
    }

    @Override
    public void delete(String path) throws Exception {
        if(path.startsWith(config.getDomain() + "/")){
            path=path.replaceFirst("^"+Pattern.quote(config.getDomain() + "/"),"");
        }
        client.removeObject(RemoveObjectArgs.builder()
                .bucket(config.getBucket()) // bucket 必须传递
                .object(path) // 相对路径作为 key
                .build());
    }
    @Override
    public void deleteList(String pathPrefix){
        try {
            // 1. 列出所有以指定前缀开头的对象
            Iterable<Result<Item>> objects = client.listObjects(ListObjectsArgs.builder()
                    .bucket(config.getBucket())
                    .prefix(pathPrefix)
                    .recursive(true)
                    .build());

            // 2. 将对象的路径添加到删除列表中
            List<DeleteObject> objectsToDelete = new ArrayList<>();
            for (Result<Item> result : objects) {
                Item item = result.get();
                objectsToDelete.add(new DeleteObject(item.objectName()));
            }

            // 3. 批量删除
            Iterable<Result<DeleteError>> results = client.removeObjects(RemoveObjectsArgs.builder()
                    .bucket(config.getBucket())
                    .objects(objectsToDelete)
                    .build());
            //4. 遍历迭代器【必须便利结果，不然不会真正删除】
            for (Result<DeleteError> result : results) {
                log.error("Error in deleting object " + result.get().objectName() + "; " + result.get().message());
            }
        } catch (Exception e) {
            log.error("批量删除有错误");
            e.printStackTrace();
        }
    }

    @Override
    public byte[] getContent(String path) throws Exception {
        GetObjectResponse response = client.getObject(GetObjectArgs.builder()
                .bucket(config.getBucket()) // bucket 必须传递
                .object(path) // 相对路径作为 key
                .build());
        return IoUtil.readBytes(response);
    }

    @Override
    public FilePresignedUrlRespDTO getPresignedObjectUrl(String path) throws Exception {
        String uploadUrl = client.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                .method(Method.PUT)
                .bucket(config.getBucket())
                .object(path)
                .expiry(10, TimeUnit.MINUTES) // 过期时间（秒数）取值范围：1 秒 ~ 7 天
                .build()
        );
        return new FilePresignedUrlRespDTO(uploadUrl, config.getDomain() + "/" + path);
    }

}
