package com.cjburkey.corestore.type.types;

import com.cjburkey.corestore.type.DataType;
import java.nio.ByteBuffer;

public final class DataTypeI8 extends DataType<Byte> {
    
    public static final short ID = 1;
    
    public int getByteSize(Byte value) {
        return Byte.BYTES;
    }
    
    public void toBytes(Byte value, ByteBuffer buffer) {
        buffer.put(value);
    }
    
    public Byte fromBytes(ByteBuffer buffer) {
        return buffer.get();
    }
    
    public Class<Byte> getType() {
        return Byte.class;
    }
    
    public short getId() {
        return ID;
    }
    
}
