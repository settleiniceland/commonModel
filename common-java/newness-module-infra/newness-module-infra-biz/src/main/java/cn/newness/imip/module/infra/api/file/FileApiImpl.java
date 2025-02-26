package cn.newness.imip.module.infra.api.file;

import cn.newness.imip.framework.common.exception.enums.GlobalErrorCodeConstants;
import cn.newness.imip.framework.common.pojo.CommonResult;
import cn.newness.imip.framework.web.core.util.WebFrameworkUtils;
import cn.newness.imip.module.infra.framework.file.config.UploadProgressManager;
import cn.newness.imip.module.infra.service.file.FileService;
import jakarta.activation.FileTypeMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import jakarta.annotation.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

/**
 * 文件 API 实现类
 *
 * @author 新奇源码
 */
@Service
@Validated
public class FileApiImpl implements FileApi {

    @Resource
    private FileService fileService;
    @Resource
    private UploadProgressManager uploadProgressManager;
    @Value("${temporary.directory}")
    private String TEMP_DIR;

    @Override
    public String createFile(String name, String path, byte[] content) {
        return fileService.createFile(name, path, content);
    }

//    老版业务都放控制层了，现搬到service层，增加复用性
//    @Override
//    public String createFile(String name, String path, BufferedInputStream inputStream, long fileLength, String type) throws Exception {
//        return fileService.createFile(name,path,inputStream,fileLength,uploadProgressManager,type);
//    }

//    新版
    @Override
    public CommonResult<String> createFile(MultipartFile file, int chunkNumber, int totalChunks, int chunkSize, int totalSize, String identifier, String fileName, String equipCode) throws Exception {
//        准备好临时文件夹
        File tempDir = new File(TEMP_DIR);
        if (!tempDir.exists()) {
            tempDir.mkdirs();
        }
//        将该分块写入到临时文件夹中
        File chunkFile = new File(tempDir, identifier + "-chunk-" + chunkNumber);
        try (FileOutputStream fos = new FileOutputStream(chunkFile)) {
            fos.write(file.getBytes());
        }
//         判断所有文件块是否都已经上传完成,完成后将文件合并为完整的文件并流式上传
        File[] chunks = tempDir.listFiles((dir, name) -> name.startsWith(identifier + "-chunk-"));
        if (chunks.length == totalChunks) {
            String url = mergeChunksAndUpload(fileName.split(",")[0], identifier, totalChunks, totalSize, equipCode.split(",")[0]);
            if(url.startsWith("http")){
                return CommonResult.success(url);
            }else {
                return CommonResult.error(GlobalErrorCodeConstants.FAIL_UPLOAD);
            }

        }
        return CommonResult.halfSuccess();
    }

    @Override
    public Integer getUpdateProgress(String userId) {
        return uploadProgressManager.getProgress(userId);
    }

    @Override
    public void deleteFile(String path) throws Exception {
        fileService.deleteFile(path);
    }

    @Override
    public void deleteFileList(String pathPrefix) throws Exception {
        fileService.deleteFileList(pathPrefix);
    }
    //合成原文件函数并上
    private String mergeChunksAndUpload(String fileName, String identifier, int totalChunks, long fileLength, String path) throws IOException {
        File tempDir = new File(TEMP_DIR);
        File finalFile = new File(TEMP_DIR, fileName);
        String url;
        try (BufferedOutputStream fos = new BufferedOutputStream(new FileOutputStream(finalFile))) {
            for (int i = 1; i <= totalChunks; i++) {
                File chunk = new File(tempDir, identifier + "-chunk-" + i);
                try (FileInputStream fis = new FileInputStream(chunk)) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = fis.read(buffer)) != -1) {
                        fos.write(buffer, 0, bytesRead);
                    }
                }
                chunk.delete();
            }
        }
//        之后采用流式上传
        try (BufferedInputStream fis = new BufferedInputStream(new FileInputStream(finalFile))) {
            String type = FileTypeMap.getDefaultFileTypeMap().getContentType(finalFile);
            url = fileService.createFile(fileName, path+"/", fis, fileLength, uploadProgressManager, type);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
//            删掉这个文件
            finalFile.delete();
        }
        return url;
    }
}
