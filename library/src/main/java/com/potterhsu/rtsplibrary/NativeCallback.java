package com.potterhsu.rtsplibrary;

public interface NativeCallback {
    void onFrame(byte[] frame, int nChannel, int width, int height);
}
