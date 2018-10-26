package com.cjburkey.corestore.type.types;

import com.cjburkey.corestore.type.DataType;
import java.nio.ByteBuffer;

/**
 * Created by CJ Burkey 2018/10/23
 * 
 * @since 0.0.1
 */
public final class DataTypeI16 extends DataType<Short> {
    
    public static final short ID = 7;
    
    public int getByteSize(Short value) {
        return Byte.BYTES;
    }
    
    public void toBytes(Short value, ByteBuffer buffer) {
        buffer.putShort(value);
    }
    
    public Short fromBytes(ByteBuffer buffer) {
        return buffer.getShort();
    }
    
    public Class<Short> getType() {
        return Short.class;
    }
    
    public short getId() {
        return ID;
    }
    
}
