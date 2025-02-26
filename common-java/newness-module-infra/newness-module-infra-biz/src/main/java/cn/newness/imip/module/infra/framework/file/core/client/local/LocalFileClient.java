package cn.newness.imip.module.infra.framework.file.core.client.local;

import cn.hutool.core.io.FileUtil;
import cn.newness.imip.module.infra.framework.file.config.UploadProgressManager;
import cn.newness.imip.module.infra.framework.file.core.client.AbstractFileClient;

import java.io.BufferedInputStream;
import java.io.File;

/**
 * 本地文件客户端
 *
 * @author 新奇源码
 */
public class LocalFileClient extends AbstractFileClient<LocalFileClientConfig> {

    public LocalFileClient(Long id, LocalFileClientConfig config) {
        super(id, config);
    }

    @Override
    protected void doInit() {
        // 补全风格。例如说 Linux 是 /，Windows 是 \
        if (!config.getBasePath().endsWith(File.separator)) {
            config.setBasePath(config.getBasePath() + File.separator);
        }
    }

    @Override
    public String upload(byte[] content, String path, String type) {
        // 执行写入
        String filePath = getFilePath(path);
        FileUtil.writeBytes(content, filePath);
        // 拼接返回路径
        return super.formatFileUrl(config.getDomain(), path);
    }

    @Override
    public String upload(BufferedInputStream inputStream, String path, String type, long fileLength, UploadProgressManager uploadProgressManager, String userId) throws Exception {
        return "";
    }

    @Override
    public void delete(String path) {
        String filePath = getFilePath(path);
        FileUtil.del(filePath);
    }

    @Override
    public byte[] getContent(String path) {
        String filePath = getFilePath(path);
        return FileUtil.readBytes(filePath);
    }

    @Override
    public void deleteList(String pathPrefix) throws Exception {

    }

    private String getFilePath(String path) {
        return config.getBasePath() + path;
    }

}
