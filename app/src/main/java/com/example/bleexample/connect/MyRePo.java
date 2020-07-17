package com.example.bleexample.connect;

import android.app.Application;
import android.bluetooth.BluetoothDevice;
import android.content.Context;

import androidx.annotation.NonNull;

import com.example.bleexample.model.DiscoveredBluetoothDevice;

import no.nordicsemi.android.ble.observer.ConnectionObserver;
import no.nordicsemi.android.log.LogSession;
import no.nordicsemi.android.log.Logger;

public class MyRePo implements ConnectionObserver {
    private MyBleManager myBleManager;
    private BluetoothDevice device;

    public MyRePo() {
    }

//    public MyRePo(@NonNull final Application application) {
//        super(application);
//
//        // Initialize the manager.
//        myBleManager = new MyBleManager(getApplication());
//    }

    public void connect(DiscoveredBluetoothDevice target, Context context){
        if (device == null){
            myBleManager = new MyBleManager(context);
            device = target.getDevice();
            final LogSession logSession = Logger
                    .newSession(context, null, target.getAddress(), target.getName());
            myBleManager.setLogSession(logSession);
            reconnect();
        }
    }

    private void reconnect() {
        if (device != null){
            myBleManager.connect(device)
                    .retry(3, 100)
                    .useAutoConnect(false)
                    .enqueue();
        }
    }

    private void disconnect(){
        device = null;
        myBleManager.disconnect().enqueue();
    }

    @Override
    public void onDeviceConnecting(@NonNull BluetoothDevice device) {

    }

    @Override
    public void onDeviceConnected(@NonNull BluetoothDevice device) {

    }

    @Override
    public void onDeviceFailedToConnect(@NonNull BluetoothDevice device, int reason) {

    }

    @Override
    public void onDeviceReady(@NonNull BluetoothDevice device) {

    }

    @Override
    public void onDeviceDisconnecting(@NonNull BluetoothDevice device) {

    }

    @Override
    public void onDeviceDisconnected(@NonNull BluetoothDevice device, int reason) {

    }
}
