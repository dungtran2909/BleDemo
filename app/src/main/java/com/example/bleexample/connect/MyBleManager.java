package com.example.bleexample.connect;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;

import androidx.annotation.NonNull;

import java.util.UUID;

import no.nordicsemi.android.ble.BleManager;
import no.nordicsemi.android.log.LogContract;
import no.nordicsemi.android.log.LogSession;
import no.nordicsemi.android.log.Logger;

public class MyBleManager extends BleManager {
//    ble service UUID
    public final static UUID BLE_UUID_SERVICE = UUID.fromString("00001523-1212-efde-1523-785feabcd123");

//    ble data text in characteristic UUID
    private final static UUID BLE_UUID_TEXT_IN = UUID.fromString("00001525-1212-efde-1523-785feabcd123");

//    ble data text out characteristic UUID
    private final static UUID BLE_UUID_TEXT_OUT = UUID.fromString("00001525-1212-efde-1523-785feabcd123");

    private BluetoothGattCharacteristic textInCharacteristic, textOnCharacteristic;
    private LogSession logSession;

    public MyBleManager(@NonNull Context context) {
        super(context);
    }

    public void setLogSession(LogSession logSession) {
        this.logSession = logSession;
    }

    @NonNull
    @Override
    protected BleManagerGattCallback getGattCallback() {
        return new MyBleGattCallback();
    }

    @Override
    public void log(int priority, @NonNull String message) {
        Logger.log(logSession, LogContract.Log.Level.fromPriority(priority), message);
    }

    private class MyBleGattCallback extends BleManagerGattCallback{
        @Override
        protected void initialize() {
            super.initialize();
        }

        @Override
        protected boolean isRequiredServiceSupported(@NonNull BluetoothGatt gatt) {
            return false;
        }

        @Override
        protected void onDeviceDisconnected() {
            textInCharacteristic = null;
            textOnCharacteristic = null;
        }
    }




}
