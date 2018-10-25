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

public class StoreIO {
    
    private static final byte[] versionBytes = newBuffer(4).putInt(WRITE_VERSION).array();
    private static final byte[] endBytes = new byte[] { (byte) 0, (byte) 0, };
    
    public static void write(@NotNull Store store, @NotNull OutputStream output, boolean closeStream, boolean pad) throws Exception {
        try {
            if (pad) {
                // Write version (ALWAYS FIRST FOUR BYTES OF BASE STORE)
                output.write(versionBytes);
            }
            getHandler().writeStore(store, output);
            if (pad) {
                // Write an empty byte to denote an empty key and thus the end of the data
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
    
    @Nullable
    public static Store read(@NotNull InputStream stream, @Nullable Store initial, boolean overwrite, boolean closeStream, boolean base) throws Exception {
        try {
            StoreHandler reader;
            if (base) {
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
    
    @Nullable
    public static Store read(@NotNull File file, @Nullable Store initial, boolean overwrite, boolean base) {
        if (!file.exists()) {
            return null;
        }
        try (FileInputStream fis = new FileInputStream(file)) {
            return read(fis, initial, overwrite, true, base);
        } catch (Exception e) {
            e.printStackTrace(ERR_OUT);
        }
        return null;
    }
    
    // -- HANLDER REGISTRATION -- //
    
    private static final Int2ObjectOpenHashMap<StoreHandler> handler = new Int2ObjectOpenHashMap<>();
    
    static {
        registerHandler(new StoreHandler0());
    }
    
    // Only meant to register readers here, and only for when the writing
    // is changed and bars the old handler from working
    private static void registerHandler(@NotNull StoreHandler reader) {
        if (!handler.containsKey(reader.getVersion())) {
            handler.put(reader.getVersion(), reader);
        }
    }
    
    @SuppressWarnings("WeakerAccess")
    @Nullable
    public static StoreHandler getHandler(int version) {
        if (handler.containsKey(version)) {
            return handler.get(version);
        }
        return null;
    }
    
    @Nullable
    public static StoreHandler getHandler() {
        return getHandler(WRITE_VERSION);
    }
    
}
