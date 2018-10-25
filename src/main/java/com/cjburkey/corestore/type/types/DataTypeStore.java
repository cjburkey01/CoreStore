package com.cjburkey.corestore.type.types;

import com.cjburkey.corestore.Store;
import com.cjburkey.corestore.io.StoreIO;
import com.cjburkey.corestore.io.core.ByteBufferBackedInputStream;
import com.cjburkey.corestore.io.core.ByteBufferBackedOutputStream;
import com.cjburkey.corestore.type.DataType;
import java.nio.ByteBuffer;

import static com.cjburkey.corestore.StoreSettings.*;

public class DataTypeStore extends DataType<Store> {
    
    public static final short ID = 9;
    
    public int getByteSize(Store store) {
        return StoreIO.getHandler().getRawSize(store);
    }
    
    public void toBytes(Store store, ByteBuffer buffer) {
        try (ByteBufferBackedOutputStream bbbos = new ByteBufferBackedOutputStream(buffer)) {
            StoreIO.write(store, bbbos, true, false);
        } catch (Exception e) {
            e.printStackTrace(ERR_OUT);
        }
    }
    
    public Store fromBytes(ByteBuffer buffer) {
        try (ByteBufferBackedInputStream bbbis = new ByteBufferBackedInputStream(buffer)) {
            return StoreIO.read(bbbis, new Store(), false, true, false);
        } catch (Exception e) {
            e.printStackTrace(ERR_OUT);
        }
        return null;
    }
    
    public Class<Store> getType() {
        return Store.class;
    }
    
    public short getId() {
        return ID;
    }
    
}
