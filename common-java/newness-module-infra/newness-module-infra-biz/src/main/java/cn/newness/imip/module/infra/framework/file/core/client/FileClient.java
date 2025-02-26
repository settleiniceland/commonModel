package cn.newness.imip.module.infra.framework.file.core.client;

import cn.newness.imip.module.infra.framework.file.config.UploadProgressManager;
import cn.newness.imip.module.infra.framework.file.core.client.s3.FilePresignedUrlRespDTO;

import java.io.BufferedInputStream;

/**
 * 文件客户端
 *
 * @author 新奇源码
 */
public interface FileClient {

    /**
     * 获得客户端编号
     *
     * @return 客户端编号
     */
    Long getId();

    /**
     * 上传文件
     *
     * @param content 文件流
     * @param path    相对路径
     * @return 完整路径，即 HTTP 访问地址
     * @throws Exception 上传文件时，抛出 Exception 异常
     */
    String upload(byte[] content, String path, String type) throws Exception;
    /**
    * 流式上传,而不是直接一个byte数组,减少内存消耗
    *
    * @param [onputStream, path, type]
    * @author machuran
    * @date 8/12/2024
    * @Return java.lang.String
    */
    String upload(BufferedInputStream inputStream, String path, String type, long fileLength, UploadProgressManager uploadProgressManager, String userId) throws Exception;
    /**
     * 删除单个文件
     *
     * @param path 相对路径
     * @throws Exception 删除文件时，抛出 Exception 异常
     */
    void delete(String path) throws Exception;
    /**
     * 批量删除文件
     *
     * @param [pathPrefix]
     * @author machuran
     * @date 8/17/2024
     * @Return void
     */
    void deleteList(String pathPrefix) throws Exception;
    /**
     * 获得文件的内容
     *
     * @param path 相对路径
     * @return 文件的内容
     */
    byte[] getContent(String path) throws Exception;

    /**
     * 获得文件预签名地址
     *
     * @param path 相对路径
     * @return 文件预签名地址
     */
    default FilePresignedUrlRespDTO getPresignedObjectUrl(String path) throws Exception {
        throw new UnsupportedOperationException("不支持的操作");
    }
}
