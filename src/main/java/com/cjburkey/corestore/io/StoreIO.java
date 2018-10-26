package com.cjburkey.corestore.io;

import com.cjburkey.corestore.Store;
import com.cjburkey.corestore.io.handler.StoreHandler;
import com.cjburkey.corestore.io.handler.handlers.StoreHandler0;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import static com.cjburkey.corestore.StoreSettings.*;
import static com.cjburkey.corestore.io.Util.*;

/**
 * Created by CJ Burkey 2018/10/25
 * 
 * @since 0.0.1
 */
public class StoreIO {
    
    // This must be first in the class so the write version is ready for usage in static members
    private static final Int2ObjectOpenHashMap<StoreHandler> handler = new Int2ObjectOpenHashMap<>();
    static {
        // Register handlers here
        registerHandler(new StoreHandler0());
    }
    
    private static final byte[] versionBytes = newBuffer(4).putInt(getWriteVersion()).array();
    private static final byte[] endBytes = new byte[] { (byte) 0, (byte) 0, };
    
    /**
     * Writes the provided store into the provided output stream
     * 
     * @since 0.0.1
     * @param store The store to write
     * @param output The output stream to which to write the provided store
     * @param closeStream Whether the stream should be closed before this method returns
     * @param pad Whether or not to write the version number and the ending pad to the end of the stream (for files only, usually)
     * @throws Exception Any exception that may be thrown during writing
     */
    public static void write(@NotNull Store store, @NotNull OutputStream output, boolean closeStream, boolean pad) throws Exception {
        try {
            if (pad) {
                // Write version (ALWAYS FIRST FOUR BYTES OF BASE STORE)
                output.write(versionBytes);
            }
            getHandler().writeStore(store, output);
            if (pad) {
                // Write an empty short to denote an empty key and thus the end of the data
                // Readers should stop reading when they hit a key of size 0
                output.write(endBytes);
            }
        } finally {
            output.flush();
            if (closeStream) {
                output.close();
            }
        }
    }
    
    /**
     * Writes the provided store into the provided output file
     * 
     * @since 0.0.1
     * @param store The store to write
     * @param output The output file to which to write the provided store
     * @return Whether the write operation was performed successfully
     */
    @SuppressWarnings("UnusedReturnValue")
    public static boolean write(@NotNull Store store, @NotNull File output) {
        if (!output.getParentFile().exists() && !output.getParentFile().mkdirs()) {
            ERR_OUT.println("Failed to create directory \"" + output.getParentFile().getAbsolutePath() + "\"");
            return false;
        }
        try (FileOutputStream fos = new FileOutputStream(output, false)) {
            write(store, fos, true, true);
            return true;
        } catch (Exception e) {
            e.printStackTrace(ERR_OUT);
        }
        return false;
    }
    
    /**
     * Reads the first store in the provided input stream into the initial store, or creates a new store with the loaded data
     * 
     * @since 0.0.1
     * @param stream The input stream
     * @param initial The store into which the loaded data will be inserted or {@code null}
     * @param overwrite Whether the loaded data should overwrite any data with the same keys in the {@code initial} store
     * @param closeStream Whether the input stream should be closed before this method returns
     * @param padded Whether the supplied stream contains padded store(s) or non-padded store(s)
     * @return The initial store unless it was {@code null}, in which case, {@code null} unless at least one key was read, then the new store
     * @throws Exception Any exception that may be thrown during reading
     */
    @Nullable
    public static Store read(@NotNull InputStream stream, @Nullable Store initial, boolean overwrite, boolean closeStream, boolean padded) throws Exception {
        try {
            StoreHandler reader;
            if (padded) {
                int v = readInt(stream);
                reader = getHandler(v);
                if (reader == null) {
                    throw new Exception("Failed to locate CoreStore reader for version: " + v + ", be sure to update CoreStore to the latest version");
                }
            } else {
                reader = getHandler();
            }
            if (reader == null) {
                throw new Exception("Failed to locate valid CoreStore reader");
            }
            return reader.readStore(stream, initial, overwrite);
        } finally {
            if (closeStream) {
                stream.close();
            }
        }
    }
    
    /**
     * Reads the provided file into the initial store or a new store if initial store is {@code null}
     * 
     * @since 0.0.1
     * @param file The file from which to load the store
     * @param initial The store into which the loaded data will be inserted or {@code null}
     * @param overwrite Whether the loaded data should overwrite any data with the same keys in the {@code initial} store
     * @param padded Whether the supplied stream contains padded store(s) or non-padded store(s)
     * @return The initial store unless it was {@code null}, in which case, {@code null} unless at least one key was read, then the new store
     */
    @Nullable
    public static Store read(@NotNull File file, @Nullable Store initial, boolean overwrite, boolean padded) {
        if (!file.exists()) {
            return null;
        }
        try (FileInputStream fis = new FileInputStream(file)) {
            return read(fis, initial, overwrite, true, padded);
        } catch (Exception e) {
            e.printStackTrace(ERR_OUT);
        }
        return null;
    }
    
    // -- HANLDER REGISTRATION -- //
    
    private static int writeVersion = 0;
    
    // Only meant to register readers here, and only for when the writing
    // is changed and bars the old handler from working
    private static void registerHandler(@NotNull StoreHandler reader) {
        if (!handler.containsKey(reader.getVersion())) {
            writeVersion = Math.max(writeVersion, reader.getVersion());
            handler.put(reader.getVersion(), reader);
        }
    }
    
    /**
     * @since 0.0.1
     * @return The newest version of
     *         the handlers present
     */
    @SuppressWarnings("WeakerAccess")
    public static int getWriteVersion() {
        return writeVersion;
    }
    
    /**
     * @since 0.0.1
     * @param version The write version for which the
     *                handler will be loaded
     * @return The handler for the given write version
     */
    @SuppressWarnings("WeakerAccess")
    @Nullable
    public static StoreHandler getHandler(int version) {
        if (handler.containsKey(version)) {
            return handler.get(version);
        }
        return null;
    }
    
    /**
     * @since 0.0.1
     * @return The handler for the latest 
     *         write version
     */
    @Nullable
    public static StoreHandler getHandler() {
        return getHandler(getWriteVersion());
    }
    
}
