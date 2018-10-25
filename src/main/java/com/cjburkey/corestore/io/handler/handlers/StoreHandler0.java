package com.cjburkey.corestore.io.handler.handlers;

import com.cjburkey.corestore.Store;
import com.cjburkey.corestore.io.handler.StoreHandler;
import com.cjburkey.corestore.type.DataType;
import com.cjburkey.corestore.type.DataTypes;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import static com.cjburkey.corestore.StoreSettings.*;
import static com.cjburkey.corestore.io.Util.*;

@SuppressWarnings("unchecked")
public class StoreHandler0 extends StoreHandler {
    
    public int getVersion() {
        return 0;
    }
    
    public int getRawSize(Store store) {
        int size = 0;
        for (String key : store.getKeys()) {
            Store.StoreValue value = getAndValidateValue(store, key);
            if (value == null) {
                continue;
            }
            
            // KeySize(Int16) + ValueType(Int16) + ValueSize(Int32) + Key + Value
            size += 2 + 2 + 4 + key.getBytes(StandardCharsets.UTF_8).length + value.dataType.getByteSize(value.data);
        }
        return size;
    }
    
    public void writeStore(Store store, OutputStream output) throws Exception {
        for (String key : store.getKeys()) {
            Store.StoreValue value = getAndValidateValue(store, key);
            if (value == null) {
                continue;
            }
            
            // Write data key size      (short)
            byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
            output.write(newBuffer(2).putShort((short) keyBytes.length).array());
            
            // Write data key
            output.write(keyBytes);
            
            // Write data value type    (short)
            output.write(newBuffer(2).putShort(value.dataType.getId()).array());
            
            // Write data value size    (int)
            int valueSize = value.dataType.getByteSize(value.data);
            output.write(newBuffer(4).putInt(valueSize).array());
            
            // Write data value
            ByteBuffer dataBuffer = newBuffer(valueSize);
            value.dataType.toBytes(value.data, dataBuffer);
            output.write(dataBuffer.array());
        }
    }
    
    @Nullable
    public Store readStore(@NotNull InputStream input, @Nullable final Store initial, boolean overwrite) throws Exception {
        // Version is already read, no need to read it here
        
        Store output = initial;
        while (true) {
            // Read key size
            short keySize = -1;
            try {
                keySize = readShort(input);
                if (keySize == 0) {
                    // Key size of zero means no more keys, stop reading here
                    break;
                }
            } catch (Exception ignored) {
            }
            if (keySize < 0) {
                break;
            }
            
            // Read key
            byte[] keyBytes = new byte[keySize];
            read(input, keyBytes);
            String key = new String(keyBytes).trim();
            if (key.isEmpty()) {
                // Key is not empty but is trimmed to nothing, skip it
                continue;
            }
            if (!overwrite && output != null && output.getHasKey(key)) {
                ERR_OUT.println("Loaded data already contains key \"" + key + "\"");
                // There is already a loaded key, skip this
                continue;
            }
            
            // Read data type
            short dataTypeId = readShort(input);
            DataType dataType = DataTypes.getHandlerById(dataTypeId);
            if (dataType == null) {
                ERR_OUT.println("Failed to locate type with ID: " + dataTypeId);
                // Unknown type, skip this
                continue;
            }
            
            // Read data value size
            int valueSize = readInt(input);
            
            // Read data value into buffer
            byte[] dataBytes = new byte[valueSize];
            read(input, dataBytes);
            ByteBuffer dataBuffer = wrapBuffer(dataBytes);
            
            // Load data value
            Object data = dataType.fromBytes(dataBuffer);
            if (data == null) {
                ERR_OUT.println("Failed to parse \"" + key + "\" with type \"" + dataType.getType().getName() + "\"");
                // The found type cannot parse this data, skip this
                continue;
            }
            
            // Store data in output storage
            if (output == null) {
                output = new Store();
            }
            if (!output.set(key, dataType, data, true)) {
                ERR_OUT.println("Failed to set \"" + key + "\"");
            }
        }
        return output;
    }
    
}
