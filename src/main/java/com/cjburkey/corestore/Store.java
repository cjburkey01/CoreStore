package com.cjburkey.corestore;

import com.cjburkey.corestore.type.DataType;
import com.cjburkey.corestore.type.DataTypes;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.Set;

import static com.cjburkey.corestore.StoreSettings.*;

/**
 * Created by CJ Burkey 2018/10/23
 * 
 * @since 0.0.1
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class Store {
    
    private final Object2ObjectOpenHashMap<String, StoreValue> data = new Object2ObjectOpenHashMap<>();
    
    /**
     * @since 0.0.1
     * @param key The key
     * @return Whether the specified key is
     *         present in this store
     */
    public boolean getHasKey(@NotNull String key) {
        return data.containsKey(key);
    }
    
    /**
     * Sets the data for the provided key to the provided value
     * 
     * @since 0.0.1
     * @param key The key of the data
     * @param type The type of the data
     * @param value The value for the data
     * @param overwrite Whether previous data under the same key should be overwritten
     * @return Whether the operation was performed successfully
     */
    public <T> boolean set(@NotNull String key, @NotNull DataType<T> type, @NotNull T value, boolean overwrite) {
        if (overwrite || !getHasKey(key)) {
            data.put(key, new StoreValue(type, value));
            return true;
        }
        return false;
    }
    
    /**
     * Sets the data for the provided key to the provided value
     * 
     * @since 0.0.1
     * @param key The key of the data
     * @param type The type of the data
     * @param value The value for the data
     * @return Whether the operation was performed successfully
     */
    public <T> boolean set(@NotNull String key, @NotNull DataType<T> type, @NotNull T value) {
        return set(key, type, value, true);
    }
    
    /**
     * Sets the data for the provided key to the provided value
     * 
     * @since 0.0.1
     * @param key The key of the data
     * @param value The value for the data
     * @param overwrite Whether previous data under the same key should be overwritten
     * @return Whether the operation was performed successfully
     */
    @SuppressWarnings("unchecked")
    public <T> boolean set(@NotNull String key, @NotNull T value, boolean overwrite) {
        DataType type = DataTypes.getHandler(value);
        if (type == null) {
            ERR_OUT.println("Failed to set data on store: failed to locate registered handler for type \"" + value.getClass().getName() + "\"");
            return false;
        }
        return set(key, (DataType<T>) type, value, overwrite);
    }
    
    /**
     * Sets the data for the provided key to the provided value
     * 
     * @since 0.0.1
     * @param key The key of the data
     * @param value The value for the data
     * @return Whether the operation was performed successfully
     */
    @SuppressWarnings("UnusedReturnValue")
    public <T> boolean set(@NotNull String key, @NotNull T value) {
        return set(key, value, true);
    }
    
    /**
     * Retrieves the data for the provided key using the provided type. Returns the default
     * value if the data was not found or the type was incorrect
     * 
     * @since 0.0.1
     * @param key The key of the data
     * @param type The type of the data
     * @param defaultValue The alternate return value
     * @return The retrieved data or {@code defaultValue} if the key was not found or the data
     *         was the incorrect type
     */
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
    
    /**
     * Retrieves the data for the provided key using the provided type. Returns the default
     * value if the data was not found or the type was incorrect
     * 
     * @since 0.0.1
     * @param key The key of the data
     * @param type The class type of the data
     * @param defaultValue The alternate return value
     * @return The retrieved data or {@code defaultValue} if the key was not found or the data
     *         was the incorrect type
     */
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
    
    /**
     * Retrieves the data for the provided key using the provided type. Returns the default
     * value if the data was not found
     * 
     * @since 0.0.1
     * @param key The key of the data
     * @param defaultValue The alternate return value
     * @return The retrieved data or {@code defaultValue} if the key was not found
     */
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
    
    /**
     * Retrieves the raw data for the provided key
     * 
     * @since 0.0.1
     * @param key The key of the data
     * @return The provided data or {@code null}
     *         if the key was not found
     */
    @Nullable
    public StoreValue getRaw(@NotNull String key) {
        if (getHasKey(key)) {
            return data.get(key);
        }
        return null;
    }
    
    /**
     * @since 0.0.1
     * @param key The key for which to retrieve the
     *            type
     * @return The type of the data at the
     *         specified key or {@code null}
     */
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
    
    /**
     * @since 0.0.1
     * @return A set of the keys
     *         present in this
     *         store
     */
    @NotNull
    public Set<String> getKeys() {
        return data.keySet();
    }
    
    /**
     * @since 0.0.1
     * @return The number
     *         of keys
     *         present in
     *         this store
     */
    public int getSize() {
        return data.size();
    }
    
    /**
     * Created by CJ Burkey 2018/10/23
     * 
     * @since 0.0.1
     */
    public static final class StoreValue {
        
        public final DataType dataType;
        public final Object data;
        
        private StoreValue(@NotNull DataType dataType, @NotNull Object data) {
            this.dataType = dataType;
            this.data = data;
        }
        
    }
    
}
