package cn.newness.imip.module.infra.framework.file.config;

import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
/**单例模式注入存储上传进度的bean
 * @author machuran
 * @date 8/13/2024
 * @time 8:22 AM
 * @Description
 */
@Component
public class UploadProgressManager {
//    线程安全类存储进度
    private final Map<String, Integer> uploadProgress = new ConcurrentHashMap<>();
//    更新进度
    public void updateProgress(String path, long bytesRead, long totalBytes) {
        uploadProgress.put(path, (int) (bytesRead * 100 / totalBytes));
    }
//    获取进度
    public Integer getProgress(String path) {
        return uploadProgress.getOrDefault(path, 0);
    }
//    上传完成移除进度
    public void removeProgress(String path) {
        uploadProgress.remove(path);
    }
}
