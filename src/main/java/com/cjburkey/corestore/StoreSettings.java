package com.cjburkey.corestore;

import java.io.PrintStream;

@SuppressWarnings("unused")
public final class StoreSettings {
    
    // CONSTANTS
    // TODO: MAKE SURE TO INCREMENT THIS IF A NEW
    //       READER IS REQUIRED TO READ THIS TYPE
    //       OF FILE AND/OR A CONVERSION NEEDS TO
    //       OCCUR ON OLD FILE TYPES
    public static final int WRITE_VERSION = 0;
    
    // Logging
    public static PrintStream STD_OUT = System.out;
    public static PrintStream ERR_OUT = System.err;
    
    // TYPES
    public static boolean REGISTER_DEFAULT_TYPES = true;
    
}
