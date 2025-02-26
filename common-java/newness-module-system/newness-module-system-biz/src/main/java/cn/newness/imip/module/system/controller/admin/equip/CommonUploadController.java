package cn.newness.imip.module.system.controller.admin.equip;

import cn.newness.imip.framework.common.exception.enums.GlobalErrorCodeConstants;
import cn.newness.imip.framework.common.pojo.CommonResult;
import cn.newness.imip.framework.web.core.util.WebFrameworkUtils;
import cn.newness.imip.module.infra.api.file.FileApi;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.activation.FileTypeMap;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;

/**
 * @author machuran
 * @date 8/11/2024
 * @time 11:18 AM
 * @Description
 */
@Tag(name = "PC端-设备相关文件上传")
@RestController
@RequestMapping("/system/equip/profile")
@Validated
@Slf4j
public class CommonUploadController {
//    @Value("${temporary.directory}")
//    private String TEMP_DIR;
    @Resource
    private FileApi fileApi;
    //文件接收方法【老版业务都在控制层，先都搬到service层，增加复用性】
//    @PostMapping(value = "/upload")
//    @Operation(summary = "分片上传设备相关文件")
//    public CommonResult<String> uploadEquipProfile(
//            @RequestParam("file") MultipartFile file,
//            @RequestParam("resumableChunkNumber") int chunkNumber,//当前分块的编号
//            @RequestParam("resumableTotalChunks") int totalChunks,//总分块数
//            @RequestParam("resumableChunkSize") int chunkSize,//每个分块的大小[以字节为单位]
//            @RequestParam("resumableTotalSize") int totalSize,//文件总大小[以字节为单位]
//            @RequestParam("resumableIdentifier") String identifier,//文件唯一标识符
//            @RequestParam("resumableFilename") String fileName,//文件名
//            @RequestParam("equipCode") String equipCode//设备档案编码【作为该设备的文件目录前缀】
//        ) throws IOException {
////        准备好临时文件夹
//        File tempDir = new File(TEMP_DIR);
//        if (!tempDir.exists()) {
//            tempDir.mkdirs();
//        }
////        将该分块写入到临时文件夹中
//        File chunkFile = new File(tempDir, identifier + "-chunk-" + chunkNumber);
//        try (FileOutputStream fos = new FileOutputStream(chunkFile)) {
//            fos.write(file.getBytes());
//        }
////         判断所有文件块是否都已经上传完成,完成后将文件合并为完整的文件并流式上传
//        File[] chunks = tempDir.listFiles((dir, name) -> name.startsWith(identifier + "-chunk-"));
//        if (chunks.length == totalChunks) {
//            String url = mergeChunksAndUpload(fileName.split(",")[0], identifier, totalChunks, totalSize, equipCode.split(",")[0]);
//            if(url.startsWith("http")){
//                return CommonResult.success(url);
//            }else {
//                return CommonResult.error(GlobalErrorCodeConstants.FAIL_UPLOAD);
//            }
//
//        }
//        return CommonResult.halfSuccess();
//    }
//    //合成原文件函数并上【先搬到service层，增加复用性】
//    private String mergeChunksAndUpload(String fileName, String identifier, int totalChunks, long fileLength, String path) throws IOException {
//        File tempDir = new File(TEMP_DIR);
//        File finalFile = new File(TEMP_DIR, fileName);
//        String url;
//        try (BufferedOutputStream fos = new BufferedOutputStream(new FileOutputStream(finalFile))) {
//            for (int i = 1; i <= totalChunks; i++) {
//                File chunk = new File(tempDir, identifier + "-chunk-" + i);
//                try (FileInputStream fis = new FileInputStream(chunk)) {
//                    byte[] buffer = new byte[1024];
//                    int bytesRead;
//                    while ((bytesRead = fis.read(buffer)) != -1) {
//                        fos.write(buffer, 0, bytesRead);
//                    }
//                }
//                chunk.delete();
//            }
//        }
////        之后采用流式上传
//        try (BufferedInputStream fis = new BufferedInputStream(new FileInputStream(finalFile))) {
//            String type = FileTypeMap.getDefaultFileTypeMap().getContentType(finalFile);
//            url = fileApi.createFile(fileName, path+"/", fis, fileLength, type);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        } finally {
////            删掉这个文件
//            finalFile.delete();
//        }
//        return url;
//    }

    @GetMapping(value = "/getUploadProgress")
    @Operation(summary = "实时查询上传进度")
    public CommonResult<Integer> getUploadProgress(){
        return CommonResult.success(fileApi.getUpdateProgress(String.valueOf(WebFrameworkUtils.getLoginUserId())));
    }
    @PostMapping(value = "/delFile")
    @Operation(summary = "从文件服务器删除文件")
    public CommonResult<String> delFile(@RequestBody List<String> paths){
        paths.forEach(path -> {
            try {
                fileApi.deleteFile(path);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        return CommonResult.success("");
    }
    //文件接收方法【新版】
    @PostMapping(value = "/upload")
    @Operation(summary = "分片上传设备相关文件")
    public CommonResult<String> uploadEquipProfile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("resumableChunkNumber") int chunkNumber,//当前分块的编号
            @RequestParam("resumableTotalChunks") int totalChunks,//总分块数
            @RequestParam("resumableChunkSize") int chunkSize,//每个分块的大小[以字节为单位]
            @RequestParam("resumableTotalSize") int totalSize,//文件总大小[以字节为单位]
            @RequestParam("resumableIdentifier") String identifier,//文件唯一标识符
            @RequestParam("resumableFilename") String fileName,//文件名
            @RequestParam("equipCode") String equipCode//设备档案编码【作为该设备的文件目录前缀】
    ) throws Exception {
        return fileApi.createFile(file,chunkNumber,totalChunks,chunkSize,totalSize,identifier,fileName,equipCode);
    }
}


