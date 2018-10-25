package com.cjburkey.corestore;

import com.cjburkey.corestore.type.DataType;
import com.cjburkey.corestore.type.DataTypes;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.Set;

import static com.cjburkey.corestore.StoreSettings.*;

@SuppressWarnings({"unused", "WeakerAccess"})
public class Store {
    
    private final Object2ObjectOpenHashMap<String, StoreValue> data = new Object2ObjectOpenHashMap<>();
    
    public boolean getHasKey(@NotNull String key) {
        return data.containsKey(key);
    }
    
    public <T> boolean set(@NotNull String key, @NotNull DataType<T> type, @NotNull T value, boolean overwrite) {
        if (overwrite || !getHasKey(key)) {
            data.put(key, new StoreValue(type, value));
            return true;
        }
        return false;
    }
    
    public <T> boolean set(@NotNull String key, @NotNull DataType<T> type, @NotNull T value) {
        return set(key, type, value, true);
    }
    
    @SuppressWarnings("unchecked")
    public <T> boolean set(@NotNull String key, @NotNull T value, boolean overwrite) {
        DataType type = DataTypes.getHandler(value);
        if (type == null) {
            ERR_OUT.println("Failed to set data on store: failed to locate registered handler for type \"" + value.getClass().getName() + "\"");
            return false;
        }
        return set(key, (DataType<T>) type, value, overwrite);
    }
    
    @SuppressWarnings("UnusedReturnValue")
    public <T> boolean set(@NotNull String key, @NotNull T value) {
        return set(key, value, true);
    }
    
    @Nullable
    public <T> T get(@NotNull String key, @NotNull DataType<T> type, @Nullable T defaultValue) {
        if (!getHasKey(key)) {
            return defaultValue;
        }
        StoreValue at = data.get(key);
        if (at == null || !at.dataType.equals(type)) {
            return defaultValue;
        }
        return type.getType().cast(at.data);
    }
    
    @Nullable
    public <T> T get(@NotNull String key, @NotNull Class<T> type, @Nullable T defaultValue) {
        if (!getHasKey(key)) {
            return defaultValue;
        }
        DataType<T> dataType = DataTypes.getHandler(type);
        if (dataType == null) {
            ERR_OUT.println("Failed to locate handler for type \"" + type.getName() + "\"");
            return defaultValue;
        }
        return get(key, dataType, defaultValue);
    }
    
    @Nullable
    public Object get(@NotNull String key, @Nullable Object defaultValue) {
        if (!getHasKey(key)) {
            return defaultValue;
        }
        StoreValue at = data.get(key);
        if (at != null) {
            return at.data;
        }
        return defaultValue;
    }
    
    @Nullable
    public StoreValue getRaw(@NotNull String key) {
        if (getHasKey(key)) {
            return data.get(key);
        }
        return null;
    }
    
    @Nullable
    public DataType<?> getType(@NotNull String key) {
        if (!getHasKey(key)) {
            return null;
        }
        StoreValue at = data.get(key);
        if (at != null) {
            return at.dataType;
        }
        return null;
    }
    
    @NotNull
    public Set<String> getKeys() {
        return data.keySet();
    }
    
    public int getSize() {
        return data.size();
    }
    
    public static final class StoreValue {
        
        public final DataType dataType;
        public final Object data;
        
        private StoreValue(@NotNull DataType dataType, @NotNull Object data) {
            this.dataType = dataType;
            this.data = data;
        }
        
    }
    
}
