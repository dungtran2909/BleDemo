package com.example.bleexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.ParcelUuid;
import android.util.Log;
import android.widget.Toast;

import com.example.bleexample.adapter.ScannerAdapter;
import com.example.bleexample.connect.MyBleManager;
import com.example.bleexample.connect.ScannerRepo;
import com.example.bleexample.imlp.ItemClickListener;
import com.example.bleexample.model.DiscoveredBluetoothDevice;

import java.util.ArrayList;
import java.util.List;

import no.nordicsemi.android.support.v18.scanner.BluetoothLeScannerCompat;
import no.nordicsemi.android.support.v18.scanner.ScanCallback;
import no.nordicsemi.android.support.v18.scanner.ScanFilter;
import no.nordicsemi.android.support.v18.scanner.ScanResult;
import no.nordicsemi.android.support.v18.scanner.ScanSettings;

public class ScannerActivity extends AppCompatActivity {
    RecyclerView rcl_scanner;
    ArrayList<DiscoveredBluetoothDevice> devices = new ArrayList<>();
    ScannerAdapter scannerAdapter;
    private int REQUEST_LOCATION_ENABLE_CODE = 1022;


    private Integer mUpdatedDeviceIndex;
    ScannerRepo scannerRepo;

    //set time scan device
    private boolean mScanning;
    private Handler mHandler;
    private final static long SCAN_DURATION = 1000000000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        initPermission();
        addControls();
        addEvents();
    }

    public void initPermission() {
        int permission_location = ContextCompat.checkSelfPermission(ScannerActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);

        if (permission_location != PackageManager.PERMISSION_GRANTED) {
            makeRequest();
        }else {
            startScan();
        }
    }

    private void makeRequest() {
        ActivityCompat.requestPermissions(ScannerActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.VIBRATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_ENABLE_CODE);
    }


    private void addEvents() {
    }

    private void addControls() {
        ItemClickListener itemClickListener = new ItemClickListener() {
            @Override
            public void onClick(int position) {

            }
        };

        rcl_scanner = findViewById(R.id.rcl_scanner);
        rcl_scanner.setLayoutManager(new LinearLayoutManager(this));
        rcl_scanner.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        scannerAdapter = new ScannerAdapter(this, devices, itemClickListener);
        rcl_scanner.setAdapter(scannerAdapter);
    }


    private void startScan(){
        if (mScanning) {
            // Extend scanning for some time more
            mHandler.removeCallbacks(mStopScanTask);
            mHandler.postDelayed(mStopScanTask, SCAN_DURATION);
            return;
        }

        BluetoothLeScannerCompat scanner = BluetoothLeScannerCompat.getScanner();
        ScanSettings settings = new ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                .setReportDelay(1000)
                .setUseHardwareBatchingIfSupported(false)
                .build();

//        List<ScanFilter> filters = new ArrayList<>();
//        filters.add(new ScanFilter.Builder().setServiceUuid(new ParcelUuid(MyBleManager.BLE_UUID_SERVICE)).build());
        scanner.startScan(null, settings, scanCallback);
    }

    private void stopScan(){
        if (!mScanning)
            return;

        final BluetoothLeScannerCompat scanner = BluetoothLeScannerCompat.getScanner();
        scanner.stopScan(scanCallback);
        mHandler.removeCallbacks(mStopScanTask);
        mScanning = false;

    }

    private Runnable mStopScanTask = new Runnable() {
        @Override
        public void run() {
            ScannerActivity.this.stopScan();
        }
    };

    private ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, @NonNull ScanResult result) {
            super.onScanResult(callbackType, result);
            Log.d("Scan", result.toString());

        }

        @Override
        public void onBatchScanResults(@NonNull List<ScanResult> results) {
            super.onBatchScanResults(results);
            Log.d("Batch", results.size()+"");
            devices.removeAll(devices);
            for (int i = 0; i < results.size(); i++){
                DiscoveredBluetoothDevice device = new DiscoveredBluetoothDevice();
                device.setDevice(results.get(i).getDevice());
                devices.add(device);
                scannerAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
            Log.d("Error", errorCode+"");
        }
    };

    public void deviceDiscovered(final ScanResult scanResult){
        DiscoveredBluetoothDevice device;

        final int index = indexOf(scanResult);
        if (index == -1) {
            device = new DiscoveredBluetoothDevice(scanResult);
            devices.add(device);
            mUpdatedDeviceIndex = null;
        } else {
            device = devices.get(index);
            mUpdatedDeviceIndex = index;
        }
        // Update RSSI and name
//        device.setRssi(result.getRssi());
        device.setName(scanResult.getScanRecord().getDeviceName());
    }

    private int indexOf(final ScanResult result) {
        int i = 0;
        for (final DiscoveredBluetoothDevice device : devices) {
            if (device.matches(result))
                return i;
            i++;
        }
        return -1;
    }


    
}