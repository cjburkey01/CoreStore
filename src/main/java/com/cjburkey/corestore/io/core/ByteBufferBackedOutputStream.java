package com.cjburkey.corestore.io.core;

import java.io.OutputStream;
import java.nio.ByteBuffer;

/**
 * Created by CJ Burkey 2018/10/25
 * 
 * This class allows using a ByteBuffer as an output stream
 * @since 0.0.1
 */
public class ByteBufferBackedOutputStream extends OutputStream {
    
    private final ByteBuffer buf;
    
    public ByteBufferBackedOutputStream(ByteBuffer buf) {
        this.buf = buf;
    }
    
    public void write(int b) {
        buf.put((byte) b);
    }
    
    public void write(byte[] bytes, int off, int len) {
        buf.put(bytes, off, len);
    }
    
}
