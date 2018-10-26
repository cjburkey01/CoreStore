package com.cjburkey.corestore.type.types;

import com.cjburkey.corestore.type.DataType;
import java.nio.ByteBuffer;

/**
 * Created by CJ Burkey 2018/10/23
 * 
 * @since 0.0.1
 */
public final class DataTypeI32 extends DataType<Integer> {
    
    public static final short ID = 5;
    
    public int getByteSize(Integer value) {
        return Integer.BYTES;
    }
    
    public void toBytes(Integer value, ByteBuffer buffer) {
        buffer.putInt(value);
    }
    
    public Integer fromBytes(ByteBuffer buffer) {
        return buffer.getInt();
    }
    
    public Class<Integer> getType() {
        return Integer.class;
    }
    
    public short getId() {
        return ID;
    }
    
}
