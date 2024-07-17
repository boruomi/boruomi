package com.boruomi.common.ftp;

import org.apache.commons.net.ftp.FTPClient;
import java.io.RandomAccessFile;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author hbh
 */
public class FTPUploader {

    private FTPClientPool ftpClientPool;
    private ExecutorService executorService;

    public FTPUploader(FTPClientPool ftpClientPool, int threadCount) {
        this.ftpClientPool = ftpClientPool;
        this.executorService = Executors.newFixedThreadPool(threadCount);
    }

    /**
     * 上传文件
     * @param file
     * @param remotePath
     * @param chunkSize
     * @throws Exception
     */
    public boolean uploadFile(File file, String remotePath, int chunkSize) throws Exception {

        FTPClient ftpClient = ftpClientPool.borrowObject();

        ensureRemotePathExists(ftpClient, remotePath);
        long remoteFileSize = getRemoteFileSize(ftpClient, remotePath);
        ftpClientPool.returnObject(ftpClient);

        long remoteChunkIndex = remoteFileSize / chunkSize;                //远程文件分片索引
        long totalChunks = (file.length() + chunkSize - 1) / chunkSize;     //上传文件的分片索引数

        for (long i = remoteChunkIndex; i < totalChunks; i++) {
            final long currentIndex = i;
            executorService.submit(() -> {
                try {
                    FTPClient client = ftpClientPool.borrowObject();
                    uploadChunk(client, file, remotePath, currentIndex, chunkSize);
                    ftpClientPool.returnObject(client);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        executorService.shutdown();
        return executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
    }

    /**
     * 获取远程文件字节大小
     * @param ftpClient
     * @param remotePath
     * @return
     * @throws IOException
     */
    private static long getRemoteFileSize(FTPClient ftpClient, String remotePath) throws IOException {
        ftpClient.enterLocalPassiveMode();
        ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);

        long remoteFileSize = ftpClient.mlistFile(remotePath).getSize();
        return remoteFileSize;
    }

    /**
     * 上传分片
     * @param ftpClient
     * @param file
     * @param remotePath
     * @param chunkIndex
     * @param chunkSize
     * @throws IOException
     */
    private void uploadChunk(FTPClient ftpClient, File file, String remotePath, long chunkIndex, int chunkSize) throws IOException {
        long offset = chunkIndex * chunkSize;
        try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
            raf.seek(offset);
            byte[] buffer = new byte[chunkSize];
            int bytesRead = raf.read(buffer);

            if (bytesRead > 0) {
                try (InputStream inputStream = new ByteArrayInputStream(buffer, 0, bytesRead)) {
                    ftpClient.appendFile(remotePath, inputStream);
                }
            }
        }
    }

    /**
     * 检查路径，不存在则创建
     * @param ftpClient
     * @param remotePath
     * @throws IOException
     */
    private void ensureRemotePathExists(FTPClient ftpClient, String remotePath) throws IOException {
        String[] directories = remotePath.split("/");
        String currentPath = "";
        for (String dir : directories) {
            if (!dir.isEmpty()) {
                currentPath += "/" + dir;
                if (!ftpClient.changeWorkingDirectory(currentPath)) {
                    ftpClient.makeDirectory(currentPath);
                }
            }
        }
    }
}
