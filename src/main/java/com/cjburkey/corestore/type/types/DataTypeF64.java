package com.cjburkey.corestore.type.types;

import com.cjburkey.corestore.type.DataType;
import java.nio.ByteBuffer;

public final class DataTypeF64 extends DataType<Double> {
    
    public static final short ID = 4;
    
    public int getByteSize(Double value) {
        return Double.BYTES;
    }
    
    public void toBytes(Double value, ByteBuffer buffer) {
        buffer.putDouble(value);
    }
    
    public Double fromBytes(ByteBuffer buffer) {
        return buffer.getDouble();
    }
    
    public Class<Double> getType() {
        return Double.class;
    }
    
    public short getId() {
        return ID;
    }
    
}
