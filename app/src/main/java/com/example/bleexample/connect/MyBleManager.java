package com.example.bleexample.connect;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.bleexample.DoBleActivity;
import com.example.bleexample.callback.TextInCallback;
import com.example.bleexample.callback.TextInDataCallback;
import com.example.bleexample.callback.TextOutCallback;
import com.example.bleexample.callback.TextOutDataCallback;

import java.util.UUID;

import no.nordicsemi.android.ble.BleManager;
import no.nordicsemi.android.ble.Request;
import no.nordicsemi.android.ble.callback.DataReceivedCallback;
import no.nordicsemi.android.ble.callback.profile.ProfileDataCallback;
import no.nordicsemi.android.ble.data.Data;
import no.nordicsemi.android.log.LogContract;
import no.nordicsemi.android.log.LogSession;
import no.nordicsemi.android.log.Logger;

public class MyBleManager extends BleManager {
//    ble service UUID
    public final static UUID BLE_UUID_SERVICE = UUID.fromString("6e400001-b5a3-f393-e0a9-e50e24dcca9e");

//    ble data text in characteristic UUID
    private final static UUID BLE_UUID_TEXT_IN = UUID.fromString("6e400002-b5a3-f393-e0a9-e50e24dcca9e");

//    ble data text out characteristic UUID
    private final static UUID BLE_UUID_TEXT_OUT = UUID.fromString("6e400003-b5a3-f393-e0a9-e50e24dcca9e");

    private BluetoothGattCharacteristic textInCharacteristic, textOutCharacteristic;
    private LogSession logSession;
    private boolean supported;

    TextOutCallback textOutCallback;

    public void setTextOutCallback(TextOutCallback textOutCallback) {
        this.textOutCallback = textOutCallback;
    }

    public MyBleManager(@NonNull Context context, TextOutCallback textOutCallback) {
        super(context);
        this.textOutCallback = textOutCallback;
    }

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

    private TextInDataCallback textInDataCallback = new TextInDataCallback() {
        @Override
        public void onTextInChanged(BluetoothDevice device, String text) {
//            DoBleActivity.textIn = text;
        }

        @Override
        public void onInvalidDataReceived(@NonNull BluetoothDevice device, @NonNull Data data) {
            log(Log.WARN, "Invalid data received: " + data);
        }

        @Override
        public void onDataSent(@NonNull BluetoothDevice device, @NonNull Data data) {
//            DoBleActivity.textIn = data.getStringValue(0);
            Log.d("SEND", "send message");
            super.onDataSent(device, data);
        }
    };

    private TextOutDataCallback textOutDataCallback = new TextOutDataCallback() {
        @Override
        public void onTextOutChanged(BluetoothDevice device, String text) {

        }

        @Override
        public void onDataReceived(@NonNull BluetoothDevice device, @NonNull Data data) {
            Log.d("READ", "read message");
            DoBleActivity.textIn = data.getStringValue(0);

        }
    };

    private class MyBleGattCallback extends BleManagerGattCallback{
        @Override
        protected boolean isRequiredServiceSupported(@NonNull BluetoothGatt gatt) {
            BluetoothGattService service = gatt.getService(BLE_UUID_SERVICE);
            if (service != null){
                textInCharacteristic = service.getCharacteristic(BLE_UUID_TEXT_IN);
                textOutCharacteristic = service.getCharacteristic(BLE_UUID_TEXT_OUT);
            }

            boolean writerequest = false;
            if (textInCharacteristic != null){
                final int properties = textInCharacteristic.getProperties();
                writerequest = (properties & BluetoothGattCharacteristic.PROPERTY_WRITE) > 0;
            }

            boolean notify = false;
            if (textOutCharacteristic != null){
                final int properties = textOutCharacteristic.getProperties();
                notify = (properties & BluetoothGattCharacteristic.PROPERTY_NOTIFY) != 0;
            }

            supported = textInCharacteristic != null && textOutCharacteristic != null && notify && writerequest;
            return supported;
        }

        @Override
        protected void initialize() {
            setNotificationCallback(textOutCharacteristic).with(textOutDataCallback);
//            writeCharacteristic(textInCharacteristic, "dungtran".getBytes()).with(textInDataCallback).enqueue();
            readCharacteristic(textOutCharacteristic).with(textInDataCallback).enqueue();
            enableNotifications(textOutCharacteristic).enqueue();

            Log.d("CHECK", textInCharacteristic.getValue()+"");
        }

        @Override
        protected void onDeviceDisconnected() {
            textInCharacteristic = null;
            textOutCharacteristic = null;
        }
    }

    public void sendText(String textIn){
        if (textInCharacteristic == null){
            return;
        }
        writeCharacteristic(textInCharacteristic, textIn.getBytes()).with(textInDataCallback).enqueue();
        readCharacteristic(textOutCharacteristic).with(textOutDataCallback).enqueue();
    }
}
