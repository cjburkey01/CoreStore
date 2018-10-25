package com.cjburkey.corestore.type;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import java.nio.ByteBuffer;
import java.util.Objects;

public abstract class DataType<T> {
    
    public abstract int getByteSize(@NotNull T value);
    
    public abstract void toBytes(@NotNull T value, @NotNull ByteBuffer buffer);
    
    @Nullable
    public abstract T fromBytes(@NotNull ByteBuffer buffer);
    
    @NotNull
    public abstract Class<T> getType();
    
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
