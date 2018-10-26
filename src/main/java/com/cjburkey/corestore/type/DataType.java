package com.cjburkey.corestore.type;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import java.nio.ByteBuffer;
import java.util.Objects;

/**
 * Created by CJ Burkey 2018/10/23
 * 
 * @since 0.0.1
 */
public abstract class DataType<T> {
    
    /**
     * @since 0.0.1
     * @param value The object for which to determine
     *              the size
     * @return The size of the provided object in
     *         bytes
     */
    public abstract int getByteSize(@NotNull T value);
    
    /**
     * Converts the provided object into bytes and writes the bytes into the
     * provided buffer
     * 
     * @since 0.0.1
     * @param value The object to be converted into bytes
     * @param buffer The buffer into which the bytes will be written
     */
    public abstract void toBytes(@NotNull T value, @NotNull ByteBuffer buffer);
    
    /**
     * Creates an object from the bytes in the provided
     * buffer
     * 
     * @since 0.0.1
     * @param buffer The buffer from which to create the object
     * @return The created object or {@code null} if
     *         creation fails
     */
    @Nullable
    public abstract T fromBytes(@NotNull ByteBuffer buffer);
    
    /**
     * @since 0.0.1
     * @return The class from which
     *         objects handled by this class are created
     */
    @NotNull
    public abstract Class<T> getType();
    
    /**
     * @since 0.0.1
     * @return The constant
     *         identifier for
     *         this type
     */
    public abstract short getId();
    
    public final boolean equals(@Nullable Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DataType that = (DataType) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getType(), that.getType());
    }
    
    public final int hashCode() {
        return Objects.hash(getId(), getType());
    }
    
}
