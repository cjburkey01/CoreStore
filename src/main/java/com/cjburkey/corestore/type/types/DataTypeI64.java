package com.cjburkey.corestore.type.types;

import com.cjburkey.corestore.type.DataType;
import java.nio.ByteBuffer;

/**
 * Created by CJ Burkey 2018/10/23
 * 
 * @since 0.0.1
 */
public final class DataTypeI64 extends DataType<Long> {
    
    public static final short ID = 6;
    
    public int getByteSize(Long value) {
        return Long.BYTES;
    }
    
    public void toBytes(Long value, ByteBuffer buffer) {
        buffer.putLong(value);
    }
    
    public Long fromBytes(ByteBuffer buffer) {
        return buffer.getLong();
    }
    
    public Class<Long> getType() {
        return Long.class;
    }
    
    public short getId() {
        return ID;
    }
    
}
