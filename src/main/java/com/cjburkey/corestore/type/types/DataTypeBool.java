package com.cjburkey.corestore.type.types;

import com.cjburkey.corestore.type.DataType;
import java.nio.ByteBuffer;

public final class DataTypeBool extends DataType<Boolean> {
    
    public static final short ID = 0;
    
    private static final byte FALSE = 0b0;
    private static final byte TRUE = 0b1;
    
    public int getByteSize(Boolean value) {
        return 1;
    }
    
    public void toBytes(Boolean value, ByteBuffer buffer) {
        buffer.put((value) ? TRUE : FALSE);
    }
    
    public Boolean fromBytes(ByteBuffer buffer) {
        return buffer.get() == TRUE;
    }
    
    public Class<Boolean> getType() {
        return Boolean.class;
    }
    
    public short getId() {
        return ID;
    }
    
}
