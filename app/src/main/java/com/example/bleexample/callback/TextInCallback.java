package com.example.bleexample.callback;

import android.bluetooth.BluetoothDevice;

public interface TextInCallback {
    void onTextInChanged(BluetoothDevice device, String text);
}
