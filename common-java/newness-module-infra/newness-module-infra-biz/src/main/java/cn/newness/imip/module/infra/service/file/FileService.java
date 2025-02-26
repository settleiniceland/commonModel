package cn.newness.imip.module.infra.service.file;

import cn.newness.imip.framework.common.pojo.PageResult;
import cn.newness.imip.module.infra.controller.admin.file.vo.file.FileCreateReqVO;
import cn.newness.imip.module.infra.controller.admin.file.vo.file.FilePageReqVO;
import cn.newness.imip.module.infra.controller.admin.file.vo.file.FilePresignedUrlRespVO;
import cn.newness.imip.module.infra.dal.dataobject.file.FileDO;
import cn.newness.imip.module.infra.framework.file.config.UploadProgressManager;

import java.io.BufferedInputStream;

/**
 * 文件 Service 接口
 *
 * @author 新奇源码
 */
public interface FileService {

    /**
     * 获得文件分页
     *
     * @param pageReqVO 分页查询
     * @return 文件分页
     */
    PageResult<FileDO> getFilePage(FilePageReqVO pageReqVO);

    /**
     * 保存文件，并返回文件的访问路径
     *
     * @param name    文件名称
     * @param path    文件路径
     * @param content 文件内容
     * @return 文件路径
     */
    String createFile(String name, String path, byte[] content);

    /**
     * 保存文件，并返回文件的访问路径[使用字节流,优化性能]
     *
     * @param [name,                path, outputStream]
     * @param uploadProgressManager
     * @param type
     * @author machuran
     * @date 8/12/2024
     * @Return java.lang.String
     */
    String createFile(String name, String path, BufferedInputStream inputStream, long fileLength, UploadProgressManager uploadProgressManager, String type) throws Exception;
    /**
     * 创建文件
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createFile(FileCreateReqVO createReqVO);

    /**
     * 删除文件
     *
     * @param id 编号
     */
    void deleteFile(Long id) throws Exception;

    /**
     * 获得文件内容
     *
     * @param configId 配置编号
     * @param path     文件路径
     * @return 文件内容
     */
    byte[] getFileContent(Long configId, String path) throws Exception;

    /**
     * 生成文件预签名地址信息
     *
     * @param path 文件路径
     * @return 预签名地址信息
     */
    FilePresignedUrlRespVO getFilePresignedUrl(String path) throws Exception;

    /**
    * 直接根据path删除文件
    *
    * @param
    * @author machuran
    * @date 8/14/2024
    * @Return
    */
    void deleteFile(String path) throws Exception;

    /**
    * 根据path前缀批量删除文件
    *
    * @param [path]
    * @author machuran
    * @date 8/17/2024
    * @Return void
    */
    void deleteFileList(String pathPrefix) throws Exception;
}
