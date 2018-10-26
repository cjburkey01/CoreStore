package com.cjburkey.corestore.io;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by CJ Burkey 2018/10/25
 * 
 * @since 0.0.1
 */
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

    /**
     * Creates a new byte buffer of the provided size
     * 
     * @since 0.0.1
     * @param size The size, in bytes, ofthe new byte buffer
     * @param direct Whether the new buffer should be direct or have a backing
     *               array
     * @param bigEndian Whether the new buffer should be marked as big endian
     * @return The allocated byte buffer
     */
    public static ByteBuffer newBuffer(int size, boolean direct, boolean bigEndian) {
        return init((direct ? ByteBuffer.allocateDirect(size) : ByteBuffer.allocate(size)), bigEndian);
    }
    
    /**
     * Creates a new byte buffer of the provided size
     * 
     * @since 0.0.1
     * @param size The size, in bytes, ofthe new
     *             byte buffer
     * @return The allocated byte buffer
     */
    public static ByteBuffer newBuffer(int size) {
        return newBuffer(size, false, true);
    }
    
    /**
     * Creates a new byte buffer around the provided array
     * 
     * @since 0.0.1
     * @param array The array of bytes to be wrapped with the new buffer
     * @param bigEndian Whether the wrapping buffer should be marked as
     *                  big endian
     * @return The wrapping byte buffer
     */
    public static ByteBuffer wrapBuffer(byte[] array, boolean bigEndian) {
        return init(ByteBuffer.wrap(array), bigEndian);
    }
    
    /**
     * Creates a new byte buffer around the provided array
     * 
     * @since 0.0.1
     * @param array The array of bytes to be wrapped
     *              with the new buffer
     * @return The wrapping byte buffer
     */
    public static ByteBuffer wrapBuffer(byte[] array) {
        return wrapBuffer(array, true);
    }
    
    /**
     * Reads a byte from the provided input stream
     * 
     * @since 0.0.1
     * @param input The stream from which to read a byte
     * @return The read byte
     * @throws IOException An exception while reading from the stream
     */
    public static byte readByte(InputStream input) throws IOException {
        byte[] read = new byte[1];
        read(input, read);
        return read[0];
    }
    
    /**
     * Reads a 16-bit integer from the provided input stream
     * 
     * @since 0.0.1
     * @param input The stream from which to read a short
     * @return The read short
     * @throws IOException An exception while reading from the stream
     */
    public static short readShort(InputStream input) throws IOException {
        byte[] read = new byte[2];
        read(input, read);
        return wrapBuffer(read).getShort();
    }
    
    /**
     * Reads a 32-bit integer from the provided input stream
     * 
     * @since 0.0.1
     * @param input The stream from which to read an int
     * @return The read int
     * @throws IOException An exception while reading from the stream
     */
    public static int readInt(InputStream input) throws IOException {
        byte[] read = new byte[4];
        read(input, read);
        return wrapBuffer(read).getInt();
    }
    
    /**
     * Reads a 64-bit integer from the provided input stream
     * 
     * @since 0.0.1
     * @param input The stream from which to read a long
     * @return The read long
     * @throws IOException An exception while reading from the stream
     */
    public static long readLong(InputStream input) throws IOException {
        byte[] read = new byte[8];
        read(input, read);
        return wrapBuffer(read).getLong();
    }
    
    /**
     * Reads a 32-bit float from the provided input stream
     * 
     * @since 0.0.1
     * @param input The stream from which to read a float
     * @return The read float
     * @throws IOException An exception while reading from the stream
     */
    public static float readFloat(InputStream input) throws IOException {
        byte[] read = new byte[4];
        read(input, read);
        return wrapBuffer(read).getFloat();
    }
    
    /**
     * Reads a 64-bit float from the provided input stream
     * 
     * @since 0.0.1
     * @param input The stream from which to read a double
     * @return The read double
     * @throws IOException An exception while reading from the stream
     */
    public static double readDouble(InputStream input) throws IOException {
        byte[] read = new byte[8];
        read(input, read);
        return wrapBuffer(read).getDouble();
    }
    
    /**
     * Reads a 2 byte character from the provided input stream
     * 
     * @since 0.0.1
     * @param input The stream from which to read a character
     * @return The read character
     * @throws IOException An exception while reading from the stream
     */
    public static char readChar(InputStream input) throws IOException {
        byte[] read = new byte[2];
        read(input, read);
        return wrapBuffer(read).getChar();
    }
    
    /**
     * Reads bytes from the input stream into the provided array of bytes
     * 
     * @since 0.0.1
     * @param input The stream from which to read the bytes
     * @param read The array into which the read bytes will be written
     * @throws IOException Occurs if the stream has fewer bytes than the array
     *                     length
     */
    public static void read(InputStream input, byte[] read) throws IOException {
        if (input.read(read) != read.length) {
            throw new IOException("No remaining bytes in stream");
        }
    }
    
}
