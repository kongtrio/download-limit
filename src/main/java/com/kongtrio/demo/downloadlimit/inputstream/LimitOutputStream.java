package com.kongtrio.demo.downloadlimit.inputstream;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author yangjb
 * @since 2019-02-28 19:21
 * <p>
 * 限流的outputstream
 */
public class LimitOutputStream extends OutputStream {

    private OutputStream outputStream;
    private BandwidthLimiter bandwidthLimiter;

    public LimitOutputStream(OutputStream outputStream, BandwidthLimiter bandwidthLimiter) {
        this.outputStream = outputStream;
        this.bandwidthLimiter = bandwidthLimiter;
    }

    @Override
    public void write(int b) throws IOException {
        if (bandwidthLimiter != null) {
            bandwidthLimiter.limitNextBytes(1);
        }
        outputStream.write(b);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        if (bandwidthLimiter != null) {
            bandwidthLimiter.limitNextBytes(len);
        }
        outputStream.write(b, off, len);
    }

    @Override
    public void write(byte[] b) throws IOException {
        if (bandwidthLimiter != null) {
            bandwidthLimiter.limitNextBytes(b.length);
        }
        outputStream.write(b);
    }
}
