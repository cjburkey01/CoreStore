package com.cjburkey.corestore.io.handler;

import com.cjburkey.corestore.Store;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class StoreHandler {
    
    public abstract int getVersion();
    public abstract int getRawSize(@NotNull Store store);
    public abstract void writeStore(@NotNull Store store, @NotNull OutputStream output) throws Exception;
    
    @Nullable
    public abstract Store readStore(@NotNull InputStream input, @Nullable Store initial, boolean overwrite) throws Exception;
    
    @SuppressWarnings("unused")
    @Nullable
    public final Store readStore(@NotNull InputStream input) throws Exception {
        return readStore(input, null, false);
    }
    
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
