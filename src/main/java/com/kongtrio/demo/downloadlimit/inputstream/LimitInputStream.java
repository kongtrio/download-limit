package com.kongtrio.demo.downloadlimit.inputstream;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author yangjb
 * @since 2018-09-14 09:58
 * <p>
 * 限流inputStream
 */
public class LimitInputStream extends InputStream {

    private InputStream inputStream;
    private BandwidthLimiter bandwidthLimiter;

    public LimitInputStream(InputStream inputStream, BandwidthLimiter bandwidthLimiter) {
        this.inputStream = inputStream;
        this.bandwidthLimiter = bandwidthLimiter;
    }

    @Override
    public int read() throws IOException {
        if (bandwidthLimiter != null) {
            bandwidthLimiter.limitNextBytes();
        }
        return inputStream.read();
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        if (bandwidthLimiter != null) {
            bandwidthLimiter.limitNextBytes(len);
        }
        return inputStream.read(b, off, len);
    }

    @Override
    public int read(byte[] b) throws IOException {
        if (bandwidthLimiter != null && b.length > 0) {
            bandwidthLimiter.limitNextBytes(b.length);
        }
        return inputStream.read(b);
    }
}
