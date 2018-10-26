package com.cjburkey.corestore.type.types;

import com.cjburkey.corestore.type.DataType;
import java.nio.ByteBuffer;

/**
 * Created by CJ Burkey 2018/10/23
 * 
 * @since 0.0.1
 */
public final class DataTypeF32 extends DataType<Float> {
    
    public static final short ID = 3;
    
    public int getByteSize(Float value) {
        return Float.BYTES;
    }
    
    public void toBytes(Float value, ByteBuffer buffer) {
        buffer.putFloat(value);
    }
    
    public Float fromBytes(ByteBuffer buffer) {
        return buffer.getFloat();
    }
    
    public Class<Float> getType() {
        return Float.class;
    }
    
    public short getId() {
        return ID;
    }
    
}
