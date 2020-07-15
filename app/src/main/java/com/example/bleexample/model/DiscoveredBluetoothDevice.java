package com.example.bleexample.model;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.Objects;

import no.nordicsemi.android.support.v18.scanner.ScanRecord;
import no.nordicsemi.android.support.v18.scanner.ScanResult;


public class DiscoveredBluetoothDevice implements Parcelable {
    private BluetoothDevice device;
    private ScanResult scanResult;
    private String name;
    private String address;

    public DiscoveredBluetoothDevice() {
    }

    public DiscoveredBluetoothDevice(final ScanResult scanResult) {
        this.scanResult = scanResult;
        this.device = scanResult.getDevice();
        final ScanRecord scanRecord =  scanResult.getScanRecord();
        if (scanRecord != null){
            this.name = scanRecord.getDeviceName();
        }
    }

    protected DiscoveredBluetoothDevice(Parcel in){
        device = in.readParcelable(BluetoothDevice.class.getClassLoader());
        scanResult = in.readParcelable(ScanResult.class.getClassLoader());
        name = in.readString();
    }

    public static final Creator<DiscoveredBluetoothDevice> CREATOR = new Creator<DiscoveredBluetoothDevice>() {
        @Override
        public DiscoveredBluetoothDevice createFromParcel(Parcel in) {
            return new DiscoveredBluetoothDevice(in);
        }

        @Override
        public DiscoveredBluetoothDevice[] newArray(int size) {
            return new DiscoveredBluetoothDevice[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(device, i);
        parcel.writeParcelable(scanResult, i);
        parcel.writeString(name);
        parcel.writeString(address);
    }

    public BluetoothDevice getDevice() {
        return device;
    }

    public void setDevice(BluetoothDevice device) {
        this.device = device;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public ScanResult getScanResult() {
        return scanResult;
    }

    public    void update(ScanResult result) {
        scanResult = result;
        name = scanResult.getScanRecord() != null ?
                scanResult.getScanRecord().getDeviceName() : null;

    }

    public boolean matches(final ScanResult scanResult) {
        return device.getAddress().equals(scanResult.getDevice().getAddress());
    }

    @Override
    public boolean equals(final Object o) {
        if (o instanceof DiscoveredBluetoothDevice) {
            final DiscoveredBluetoothDevice that = (DiscoveredBluetoothDevice) o;
            return device.getAddress().equals(that.device.getAddress());
        }
        return super.equals(o);
    }
}
