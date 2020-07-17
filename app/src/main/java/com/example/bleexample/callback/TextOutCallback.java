package com.example.bleexample.callback;

import android.bluetooth.BluetoothDevice;

public interface TextOutCallback {
    void onTextOutChanged(BluetoothDevice device, String text);
}
