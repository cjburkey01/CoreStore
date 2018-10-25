package com.cjburkey.corestore.io;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

@SuppressWarnings({"WeakerAccess", "unused"})
public final class Util {
    
    private static ByteBuffer init(ByteBuffer buffer, boolean bigEndian) {
        // Set to specified endian-ness
        buffer.order(bigEndian ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN);
        
        // Reset buffer position and limit
        buffer.position(0);
        buffer.limit(buffer.capacity());
        
        return buffer;
    }
    
    public static ByteBuffer newBuffer(int size, boolean direct, boolean bigEndian) {
        return init((direct ? ByteBuffer.allocateDirect(size) : ByteBuffer.allocate(size)), bigEndian);
    }
    
    public static ByteBuffer newBuffer(int size) {
        return newBuffer(size, false, true);
    }
    
    public static ByteBuffer wrapBuffer(byte[] array, boolean bigEndian) {
        return init(ByteBuffer.wrap(array), bigEndian);
    }
    
    public static ByteBuffer wrapBuffer(byte[] array) {
        return wrapBuffer(array, true);
    }
    
    public static byte readByte(InputStream input) throws IOException {
        byte[] read = new byte[1];
        read(input, read);
        return read[0];
    }
    
    public static short readShort(InputStream input) throws IOException {
        byte[] read = new byte[2];
        read(input, read);
        return wrapBuffer(read).getShort();
    }
    
    public static int readInt(InputStream input) throws IOException {
        byte[] read = new byte[4];
        read(input, read);
        return wrapBuffer(read).getInt();
    }
    
    public static long readLong(InputStream input) throws IOException {
        byte[] read = new byte[8];
        read(input, read);
        return wrapBuffer(read).getLong();
    }
    
    public static float readFloat(InputStream input) throws IOException {
        byte[] read = new byte[4];
        read(input, read);
        return wrapBuffer(read).getFloat();
    }
    
    public static double readDouble(InputStream input) throws IOException {
        byte[] read = new byte[8];
        read(input, read);
        return wrapBuffer(read).getDouble();
    }
    
    public static char readChar(InputStream input) throws IOException {
        byte[] read = new byte[2];
        read(input, read);
        return wrapBuffer(read).getChar();
    }
    
    public static void read(InputStream input, byte[] read) throws IOException {
        if (input.read(read) != read.length) {
            throw new IOException("No remaining bytes in stream");
        }
    }
    
}
