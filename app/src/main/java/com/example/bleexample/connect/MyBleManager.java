package com.example.bleexample.connect;

import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;

import androidx.annotation.NonNull;

import java.util.UUID;

import no.nordicsemi.android.ble.BleManager;

public class MyBleManager extends BleManager {
//    ble service UUID
    public final static UUID BLE_UUID_SERVICE = UUID.fromString("00001523-1212-efde-1523-785feabcd123");

//    ble data text in characteristic UUID
    private final static UUID BLE_UUID_TEXT_IN = UUID.fromString("00001525-1212-efde-1523-785feabcd123");

//    ble data text out characteristic UUID
    private final static UUID BLE_UUID_TEXT_OUT = UUID.fromString("00001525-1212-efde-1523-785feabcd123");

    private BluetoothGattCharacteristic textInCharacteristic, textOnCharacteristic;

    public MyBleManager(@NonNull Context context) {
        super(context);
    }

    @NonNull
    @Override
    protected BleManagerGattCallback getGattCallback() {
        return null;
    }
}
