package com.cjburkey.corestore.type.types;

import com.cjburkey.corestore.type.DataType;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Created by CJ Burkey 2018/10/23
 * 
 * @since 0.0.1
 */
public final class DataTypeString extends DataType<String> {
    
    public static final short ID = 8;
    private static final Charset charset = StandardCharsets.UTF_8;
    
    public int getByteSize(String value) {
        return value.getBytes(charset).length;
    }
    
    public void toBytes(String value, ByteBuffer buffer) {
        buffer.put(value.getBytes(charset));
    }
    
    public String fromBytes(ByteBuffer buffer) {
        return new String(buffer.array(), charset);
    }
    
    public Class<String> getType() {
        return String.class;
    }
    
    public short getId() {
        return ID;
    }
    
}
