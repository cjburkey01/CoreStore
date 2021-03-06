package com.cjburkey.corestore.type.types;

import com.cjburkey.corestore.type.DataType;
import java.nio.ByteBuffer;

/**
 * Created by CJ Burkey 2018/10/25
 * 
 * @since 0.0.1
 */
public final class DataTypeChar extends DataType<Character> {
    
    public static final short ID = 2;
    
    public int getByteSize(Character value) {
        return Character.BYTES;
    }
    
    public void toBytes(Character value, ByteBuffer buffer) {
        buffer.putChar(value);
    }
    
    public Character fromBytes(ByteBuffer buffer) {
        return buffer.getChar();
    }
    
    public Class<Character> getType() {
        return Character.class;
    }
    
    public short getId() {
        return ID;
    }
    
}
