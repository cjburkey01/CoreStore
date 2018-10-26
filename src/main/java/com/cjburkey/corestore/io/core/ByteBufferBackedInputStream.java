package com.cjburkey.corestore.io.core;

import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * Created by CJ Burkey 2018/10/25
 * 
 * This class allows using a ByteBuffer as an input stream
 * @since 0.0.1
 */
public class ByteBufferBackedInputStream extends InputStream {
    
    private final ByteBuffer buf;
    
    public ByteBufferBackedInputStream(ByteBuffer buf) {
        this.buf = buf;
    }
    
    public int read() {
        if (!buf.hasRemaining()) {
            return -1;
        }
        return buf.get() & 0xFF;
    }
    
    public int read(byte[] bytes, int off, int len) {
        if (!buf.hasRemaining()) {
            System.out.println(buf.position() + " / " + buf.limit());
            return -1;
        }
        len = Math.min(len, buf.remaining());
        buf.get(bytes, off, len);
        return len;
    }
    
}
