package ftp;

import com.boruomi.common.ftp.FTPClientPool;
import com.boruomi.common.ftp.FTPUploader;
import org.junit.jupiter.api.Test;

import java.io.File;

public class FtpTest {
    @Test
    public void test1() throws Exception {
        String server = "47.94.14.142";
        int port = 21;
        String user = "root";
        String password = "Secretcode0";
        int maxTotal = 10; // 连接池最大连接数

        FTPClientPool ftpClientPool = FTPClientPool.create(server, port, user, password, maxTotal);

        FTPUploader uploader = new FTPUploader(ftpClientPool,4);
        File file = new File("path/to/local/file");
        String remotePath = "/path/to/remote/file";
        int chunkSize = 1024 * 1024; // 每块1MB

        boolean b = uploader.uploadFile(file, remotePath, chunkSize);
    }
}
