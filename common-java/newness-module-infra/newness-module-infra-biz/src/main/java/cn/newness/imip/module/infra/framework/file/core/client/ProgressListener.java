package cn.newness.imip.module.infra.framework.file.core.client;

/** 文件上传进度监听器
 * @author machuran
 * @date 8/13/2024
 * @time 8:46 AM
 * @Description
 */
public interface ProgressListener {
    void onProgress(long bytesRead, long totalBytes);
}
