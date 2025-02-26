package cn.newness.imip.module.infra.framework.file.core.client;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
/** 文件上传进度监听器专用输入流
 * @author machuran
 * @date 8/13/2024
 * @time 8:40 AM
 * @Description
 */
public class ProgressInputStream extends BufferedInputStream {
    private long bytesRead = 0;
    private final long totalBytes;
    private final ProgressListener listener;

    public ProgressInputStream(InputStream in, long totalBytes, ProgressListener listener) {
        super(in);
        this.totalBytes = totalBytes;
        this.listener = listener;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int bytesRead = super.read(b, off, len);
        if (bytesRead > 0) {
            this.bytesRead += bytesRead;
            listener.onProgress(this.bytesRead, this.totalBytes);
        }
        return bytesRead;
    }
}

