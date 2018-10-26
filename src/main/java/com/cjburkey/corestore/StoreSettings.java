package com.cjburkey.corestore;

import java.io.PrintStream;

/**
 * Created by CJ Burkey 2018/10/23
 * 
 * The main settings file for CoreStore.
 * 
 * Altering settings  after initialization (first-time usage)
 * of CoreStore may induce unexpected behavior.
 * 
 * @since 0.0.1
 */
@SuppressWarnings("unused")
public final class StoreSettings {
    
    // Logging
    public static PrintStream STD_OUT = System.out;
    public static PrintStream ERR_OUT = System.err;
    
    // TYPES
    public static boolean REGISTER_DEFAULT_TYPES = true;
    
}
