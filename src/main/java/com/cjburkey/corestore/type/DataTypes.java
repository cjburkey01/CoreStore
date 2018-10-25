package com.cjburkey.corestore.type;

import com.cjburkey.corestore.type.types.DataTypeBool;
import com.cjburkey.corestore.type.types.DataTypeChar;
import com.cjburkey.corestore.type.types.DataTypeF32;
import com.cjburkey.corestore.type.types.DataTypeF64;
import com.cjburkey.corestore.type.types.DataTypeI16;
import com.cjburkey.corestore.type.types.DataTypeI32;
import com.cjburkey.corestore.type.types.DataTypeI64;
import com.cjburkey.corestore.type.types.DataTypeI8;
import com.cjburkey.corestore.type.types.DataTypeStore;
import com.cjburkey.corestore.type.types.DataTypeString;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.shorts.Short2ObjectOpenHashMap;

import static com.cjburkey.corestore.StoreSettings.*;

@SuppressWarnings({"WeakerAccess", "unchecked"})
public final class DataTypes {
    
    private static boolean init = false;
    private static final Object2ObjectOpenHashMap<String, DataType<?>> registeredTypeHandlersByClass = new Object2ObjectOpenHashMap<>();
    private static final Short2ObjectOpenHashMap<DataType<?>> registeredTypeHandlersById = new Short2ObjectOpenHashMap<>();
    
    public static <T> boolean getHasHandler(@NotNull Class<T> type) {
        tryInit();
        return registeredTypeHandlersByClass.containsKey(type.getName());
    }
    
    private static <T> boolean registerHandler(@NotNull DataType<T> handler, boolean optimize) {
        tryInit();
        if (getHasHandler(handler.getType())) {
            return false;
        }
        if (registeredTypeHandlersById.containsKey(handler.getId())) {
            ERR_OUT.println("Failed to register data type handler: found data handler for type \"" + registeredTypeHandlersById.get(handler.getId()) + "\" with same ID as for type \"" + handler.getType().getName() + "\" with ID of " + handler.getId());
            return false;
        }
        registeredTypeHandlersByClass.put(handler.getType().getName(), handler);
        registeredTypeHandlersById.put(handler.getId(), handler);
        if (optimize) {
            registeredTypeHandlersByClass.trim();
        }
        STD_OUT.println("Registered handler for \"" + handler.getType().getName() + "\"");
        return true;
    }
    
    @SuppressWarnings("unused")
    private static <T> boolean registerHandler(@NotNull DataType<T> handler) {
        return registerHandler(handler, true);
    }
    
    @Nullable
    public static <T> DataType<T> getHandler(@NotNull Class<T> type) {
        tryInit();
        if (getHasHandler(type)) {
            return (DataType<T>) registeredTypeHandlersByClass.get(type.getName());
        }
        return null;
    }
    
    @Nullable
    public static <T> DataType<T> getHandler(@NotNull T value) {
        return (DataType<T>) getHandler(value.getClass());
    }
    
    @Nullable
    public static DataType<?> getHandlerById(short dataTypeId) {
        tryInit();
        if (registeredTypeHandlersById.containsKey(dataTypeId)) {
            return registeredTypeHandlersById.get(dataTypeId);
        }
        return null;
    }
    
    // -- DEFAULT HANDLERS -- //
    
    public static final DataTypeBool BOOLEAN = new DataTypeBool();
    public static final DataTypeI8 BYTE = new DataTypeI8();
    public static final DataTypeChar CHAR = new DataTypeChar();
    public static final DataTypeF32 FLOAT = new DataTypeF32();
    public static final DataTypeF64 DOUBLE = new DataTypeF64();
    public static final DataTypeI32 INT = new DataTypeI32();
    public static final DataTypeI64 LONG = new DataTypeI64();
    public static final DataTypeString STRING = new DataTypeString();
    public static final DataTypeStore STORE = new DataTypeStore();
    public static final DataTypeI16 SHORT = new DataTypeI16();
    
    private static void tryInit() {
        if (init || !REGISTER_DEFAULT_TYPES) {
            return;
        }
        init = true;
        
        registerHandler(BOOLEAN, false);
        registerHandler(BYTE, false);
        registerHandler(CHAR, false);
        registerHandler(FLOAT, false);
        registerHandler(DOUBLE, false);
        registerHandler(INT, false);
        registerHandler(LONG, false);
        registerHandler(STRING, false);
        registerHandler(STORE, false);
        registerHandler(SHORT, true);
    }
}
