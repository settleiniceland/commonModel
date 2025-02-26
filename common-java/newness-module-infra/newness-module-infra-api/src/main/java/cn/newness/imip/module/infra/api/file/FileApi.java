package cn.newness.imip.module.infra.api.file;

import cn.newness.imip.framework.common.pojo.CommonResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;

/**
 * 文件 API 接口
 *
 * @author 新奇源码
 */
public interface FileApi {

    /**
     * 保存文件，并返回文件的访问路径
     *
     * @param content 文件内容
     * @return 文件路径
     */
    default String createFile(byte[] content) {
        return createFile(null, null, content);
    }

    /**
     * 保存文件，并返回文件的访问路径
     *
     * @param path 文件路径
     * @param content 文件内容
     * @return 文件路径
     */
//    default String createFile(String path, byte[] content) {
//        return createFile(null, path, content);
//    }

    /**
     * 保存文件，并返回文件的访问路径
     *
     * @param name 文件名称
     * @param path 文件路径
     * @param content 文件内容
     * @return 文件路径
     */
    String createFile(String name, String path, byte[] content);
    /**
     * 保存文件，并返回文件的访问路径[流式]
     *
     * @return 文件路径
     * @author machuran
     */
//    default String createFile(BufferedInputStream inputStream, long fileLength, String type) throws Exception {
//        return createFile(null, null, inputStream, fileLength, type);
//    }

    /**
     * 保存文件，并返回文件的访问路径[流式]
     *
     * @return 文件路径
     * @author machuran
     */
//    default String createFile(String path, BufferedInputStream inputStream, long fileLength, String type) throws Exception {
//        return createFile(null, path, inputStream, fileLength, type);
//    }

//    /**
//     * 保存文件，并返回文件的访问路径[流式]【老版业务都在控制层，先都搬到service层，增加复用性】
//     *
//     * @return 文件路径
//     * @author machuran
//     */
//    String createFile(String name, String path, BufferedInputStream inputStream, long fileLength, String type) throws Exception;
    /**
     * 保存文件，并返回文件的访问路径[流式]【新版】
     *
     * @return 文件路径
     * @author machuran
     */
    CommonResult<String> createFile(MultipartFile file, int chunkNumber, int totalChunks, int chunkSize, int totalSize, String identifier, String fileName, String equipCode) throws Exception;
    /**
    * 从单例中获取到自己的进度
    *
    * @param []
    * @author machuran
    * @date 8/13/2024
    * @Return java.lang.Integer
    */
    Integer getUpdateProgress(String path);
    /**
    * 删除文件
    *
    * @param
    * @author machuran
    * @date 8/14/2024
    * @Return
    */
    void deleteFile(String path) throws Exception;
    /**
     * 批量删除文件
     *
     * @param
     * @author machuran
     * @date 8/14/2024
     * @Return
     */
    void deleteFileList(String pathPrefix) throws Exception;
}
