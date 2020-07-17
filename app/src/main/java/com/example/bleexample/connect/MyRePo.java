package com.example.bleexample.connect;

import android.bluetooth.BluetoothDevice;
import android.content.Context;

import com.example.bleexample.model.DiscoveredBluetoothDevice;

import no.nordicsemi.android.log.LogSession;
import no.nordicsemi.android.log.Logger;

public class MyRePo {
    private MyBleManager myBleManager;
    private BluetoothDevice device;

    public MyRePo(MyBleManager myBleManager) {
        this.myBleManager = myBleManager;
    }

    public MyRePo() {
    }

    public void connect(DiscoveredBluetoothDevice target, Context context){
        if (device == null){
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
}
