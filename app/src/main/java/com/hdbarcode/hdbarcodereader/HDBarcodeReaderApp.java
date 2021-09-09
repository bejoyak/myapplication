package com.hdbarcode.hdbarcodereader;

import java.nio.ByteBuffer;

public class HDBarcodeReaderApp {

    public static  native int DecodeHDBarcode(ByteBuffer directBuffer, int sizeX, int sizeY);

    static {
        System.loadLibrary("hdbarcode");
    }
}
