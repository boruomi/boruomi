package com.boruomi.common.ftp;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * @author hbh
 */
public class FTPClientPool extends GenericObjectPool<FTPClient> {

    private FTPClientPool(GenericObjectPoolConfig<FTPClient> config, FTPClientFactory factory) {
        super(factory, config);
    }

    public static FTPClientPool create(String server, int port, String user, String password, int maxTotal) {
        FTPClientFactory factory = FTPClientFactory.create(server, port, user, password);
        GenericObjectPoolConfig<FTPClient> config = new GenericObjectPoolConfig<>();
        config.setMaxTotal(maxTotal);
        return new FTPClientPool(config, factory);
    }
}
