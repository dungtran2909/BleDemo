package com.example.bleexample.connect;

import android.os.ParcelUuid;

import androidx.annotation.NonNull;

import com.example.bleexample.model.DiscoveredBluetoothDevice;

import java.util.ArrayList;
import java.util.List;

import no.nordicsemi.android.support.v18.scanner.ScanRecord;
import no.nordicsemi.android.support.v18.scanner.ScanResult;

public class ScannerRepo {
    private static final ParcelUuid FILTER_UUID = new ParcelUuid(MyBleManager.BLE_UUID_SERVICE);

    private final List<DiscoveredBluetoothDevice> devices = new ArrayList<>();

    private List<DiscoveredBluetoothDevice> filteredDevices = null;
    private boolean filterRequired;

    public boolean deviceDiscovered(@NonNull final ScanResult result) {
        DiscoveredBluetoothDevice device;

        // Check if it's a new device.
        final int index = indexOf(result);
        if (index == -1) {
            device = new DiscoveredBluetoothDevice(result);
            devices.add(device);
        } else {
            device = devices.get(index);
        }

        // Update RSSI and name.
        device.update(result);

        // Return true if the device was on the filtered list or is to be added.
        return (filteredDevices != null && filteredDevices.contains(device))
                || (matchesFilter(result));
    }

    public synchronized void clear() {
        devices.clear();
        filteredDevices = null;
    }

    public synchronized boolean applyFilter() {
        final List<DiscoveredBluetoothDevice> tmp = new ArrayList<>();
        for (final DiscoveredBluetoothDevice device : devices) {
            final ScanResult result = device.getScanResult();
            if (matchesFilter(result)) {
                tmp.add(device);
            }
        }
        filteredDevices = tmp;
        return !filteredDevices.isEmpty();
    }

    private int indexOf(@NonNull final ScanResult result) {
        int i = 0;
        for (final DiscoveredBluetoothDevice device : devices) {
            if (device.matches(result))
                return i;
            i++;
        }
        return -1;
    }

    private boolean matchesFilter(@NonNull final ScanResult result) {
        if (!filterRequired)
            return true;

        final ScanRecord record = result.getScanRecord();
        if (record == null)
            return false;

        final List<ParcelUuid> uuids = record.getServiceUuids();
        if (uuids == null)
            return false;

        return uuids.contains(FILTER_UUID);
    }
}
