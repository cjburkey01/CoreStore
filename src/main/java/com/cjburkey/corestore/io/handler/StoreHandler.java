package com.cjburkey.corestore.io.handler;

import com.cjburkey.corestore.Store;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by CJ Burkey 2018/10/23
 * 
 * @since 0.0.1
 */
public abstract class StoreHandler {
    
    /**
     * @since 0.0.1
     * @return The version which this storage handler will handle (the write version)
     */
    public abstract int getVersion();
    
    /**
     * @since 0.0.1
     * @param store The store for which to determine the size
     * @return The size, in bytes, of the store
     */
    public abstract int getRawSize(@NotNull Store store);
    
    /**
     * Writes the provided store into the provided output stream. This output stream could be a
     * {@code FileOutputStream} or any output stream that supports the basic read operations
     * 
     * @since 0.0.1
     * @param store The store to write to the provided stream
     * @param output The destination for the provided store
     * @throws Exception Any exception that may be thrown during writing
     */
    public abstract void writeStore(@NotNull Store store, @NotNull OutputStream output) throws Exception;
    
    /**
     * Reads a store object from the supplied input stream
     * 
     * @since 0.0.1
     * @param input The stream from which to read the storage
     * @param initial The data store to which the read data will be appended. If this is null, then a new store is created if
     *                any data is found
     * @param overwrite Whether existing data in the {@code initial} store should be overwritten if a duplicate key is found
     * @return The initial store with the read data. If the initial store was {@code null}, then a new store unless no keys
     *         were successfully read from the stream, in which case, {@code null}
     * @throws Exception Any exception that may be thrown during reading
     */
    @Nullable
    public abstract Store readStore(@NotNull InputStream input, @Nullable Store initial, boolean overwrite) throws Exception;
    
    /**
     * Reads a store object from the supplied input stream
     * 
     * @since 0.0.1
     * @param input The stream from which to read the storage
     * @return A new store unless no keys were successfully read from the stream, in which case,
     *         {@code null}
     * @throws Exception Any exception that may be thrown during reading
     */
    @SuppressWarnings("unused")
    @Nullable
    public final Store readStore(@NotNull InputStream input) throws Exception {
        return readStore(input, null, false);
    }
    
    /**
     * Retrieves and validates the value of the specified key in the specified data store
     * 
     * @since 0.0.1
     * @param store The store from which to read the data of the provided key
     * @param key The key from which to read the data in the provided store
     * @return The valid data/type pair at the provided key, or null if it doesn't exist or fails
     *         validation
     */
    protected static Store.StoreValue getAndValidateValue(@NotNull Store store, @NotNull String key) {
        if (key.trim().isEmpty()) {
            return null;
        }
        Store.StoreValue value = store.getRaw(key);
        if (value == null || value.dataType == null || value.data == null || !value.data.getClass().equals(value.dataType.getType())) {
            return null;
        }
        return value;
    }
    
}
